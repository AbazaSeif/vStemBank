<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Home
 *
 * @author Seif Abaza <Telegram @Seif_Abaza1>
 */
class Home extends MY_Controller {

    public function __construct($config = array()) {
        parent::__construct($config);
        if (is_null($this->session->name)) {
            redirect(base_url());
        }
    }

    public function homepage($ID = null) {
        $Data = $this->getBasicData();
        $TetcherGroups = $this->getTetcherGroups($this->session->id);
        if (!is_null($TetcherGroups)) {
            $Data['Groplist'] = $TetcherGroups;
        } else {
            $Data['Groplist'] = null;
        }
        if (is_null($ID)) {
            $DataCheck = $this->workgroups->get(['tetcher' => $this->session->id, 'status' => 1])[0];
            if (!is_null($DataCheck)) {
                $GroupName = $this->getGroupsWhere(['id' => $DataCheck->groupid])[0]->groupname;
                $IDG = $DataCheck->id;
                $DataCheck->name = $GroupName;
                $this->session->set_userdata('sessionIDwork', $IDG);
                $this->session->set_userdata('sessionwork', get_object_vars($DataCheck));
                $ID = $DataCheck->groupid;
            } else {
                $ID = $TetcherGroups[0]->id;
                if (is_null($ID)) {
                    $ID = null;
                }
            }
        }
        if (!is_null($ID)) {
            $DataGroupID = $this->tusersg->get(['groupid' => $ID]);
            if (!is_null($DataGroupID)) {
                $Users = array();
                foreach ($DataGroupID as $UserID) {
                    $User = $this->getStudintInfo(['id' => $UserID->userid]);
                    if (!is_null($User)) {
                        $Amount = $this->getAmount($User->id);
                        $User->amount = $Amount;
                        array_push($Users, $User);
                    }
                }
                if (!is_null($Users)) {
                    $Data['GSelect'] = $ID;
                    $Data['uGroup'] = $Users;
                    $Data['cStart'] = true;
                } else {
                    $Data['GSelect'] = $ID;
                    $Data['uGroup'] = null;
                    $Data['cStart'] = false;
                }
            } else {
                $Data['GSelect'] = $ID;
                $Data['uGroup'] = null;
                $Data['cStart'] = false;
            }
        } else {
            $Data['GSelect'] = $ID;
            $Data['uGroup'] = null;
            $Data['cStart'] = false;
        }
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('lessons', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function IncrimentAmount() {
        $Data = $this->input->post();
        $usedid = $Data['userid'];
        $Amount = $Data['valueinput'];
        if (empty($Amount)) {
            $this->setMessage('error', "Баланс не обновлен");
            $this->homepage($Data['groupid']);
        }
        if ($this->setIncrimentAmount($usedid, $Amount)) {
            $this->setDincrimentAmount($this->session->id, $Amount);
            $this->setMessage('success', "Баланс обновлен");
        } else {
            $this->setMessage('error', "Баланс не обновлен");
        }
        $this->homepage($Data['groupid']);
    }

    public function DincrimentAmount() {
        $Data = $this->input->post();
        $usedid = $Data['useridd'];
        $Amount = $Data['valueinput'];
        if (empty($Amount)) {
            $this->setMessage('error', "Баланс не обновлен");
            $this->homepage($Data['groupids']);
        }
        if ($this->setDincrimentAmount($usedid, $Amount)) {
            $this->setIncrimentAmount($this->session->id, $Amount);
            $this->setMessage('success', "Баланс обновлен");
        } else {
            $this->setMessage('error', "Баланс не обновлен");
        }
        $this->homepage($Data['groupid']);
    }

    public function checktimeforamount() {
        $groupid = $this->session->sessionIDwork;
        if ((!is_null($groupid)) || ($groupid != 0)) {
            $GData = $this->workgroups->get(['id' => $groupid, 'status' => 1])[0];
            $StartTime = new DateTime($GData->timestart);
            $TimeNow = new DateTime(date("Y/m/d H:i:s"));
            $Dt = $this->dateDifference($GData->timestart, date("Y/m/d H:i:s"));
            $invter = $StartTime->diff($TimeNow);
            $Time = $invter->format('%h') . ':' . $invter->format('%i') . ':' . $invter->format('%s');
            $TimeNeed = "0:" . $this->getSetting()->longcharge . ":0";
            if ($Time == $TimeNeed) {
                $AmountStart = $this->getSetting()->startsession;
                $this->setIncrimentAmount($this->session->id, $AmountStart);
            } else {
                $AmountStart = $this->getAmount($this->session->id);
            }

            $TimeForEnd = $this->getSetting()->endlesson . ":0:0";
//            $TimeForEnd = "0:" . $this->getSetting()->endlesson . ":0";
            if (strcmp($Time, $TimeForEnd) > 0) {
                $groupid = $this->session->sessionIDwork;
                $this->endlesson();
                redirect(base_url() . 'home/' . $groupid);
            } else {
                echo json_encode(['time' => $Dt, 'amount' => $AmountStart, 'end' => 1]);
            }
        }
    }

    public function startlesson() {
        $label = $this->input->post('lbl');
        $groupid = $this->input->post('gro');
        $GroupName = $this->getGroupsWhere(['id' => $groupid])[0]->groupname;
        $Data = [
            'groupid' => $groupid,
            'tetcher' => $this->session->id,
            'label' => $label,
            'timestart' => date("Y/m/d H:i:s"),
            'timeend' => '00:00:00',
            'status' => 1,
            'balance' => $this->getAmount($this->session->id)
        ];

        $IDG = $this->workgroups->set($Data);
        $Data['name'] = $GroupName;
        $this->session->set_userdata('sessionIDwork', $IDG);
        $this->session->set_userdata('sessionwork', $Data);

        echo $this->getAmount($this->session->id);
    }

    public function endlesson() {
        $groupid = $this->input->post('gro');

        $Data = [
            'timeend' => date("Y/m/d H:i:s"),
            'status' => 0,
            'balance' => $this->getAmount($this->session->id)
        ];

        $this->setResetAmount($this->session->id);
        $GroupWorkID = $this->session->sessionIDwork;
        $this->UpdateFactorTableForGroup($GroupWorkID);
        $this->workgroups->update($Data, ['groupid' => $groupid, 'status' => 1, 'tetcher' => $this->session->id]);
        $this->session->unset_userdata('sessionwork');
        $this->session->unset_userdata('sessionIDwork');
        echo $this->getAmount($this->session->id);
    }

    public function reportpage() {
        $Data = $this->getBasicData();
        $Groups = $this->getGroups();
        $Data['Groplistgeneral'] = $Groups;
        $Tetchers = $this->getStudints();
        $Data['tetcher'] = $Tetchers;

        $DataPass = $this->session->groupreport;
        if (!is_null($DataPass)) {
            $Data['dReport'] = $DataPass;
            $this->session->unset_userdata('groupreport');
        } else {
            $Data['dReport'] = null;
        }
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('report', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function getGroupReport() {
        $DataReportSearch = $this->input->post();
        $StudentName = $DataReportSearch['studname'];
        if ($StudentName != 'BCE') {
            $StudID = $this->getStudintInfo(['name' => $StudentName])->id;
        } else {
            $StudID = -1;
        }
        $GroupID = $DataReportSearch['groupid'];
        $ListReport = $this->getReportOfThisStudent($StudID, $GroupID);
        $this->session->set_userdata('groupreport', $ListReport);
        echo true;
    }

    public function getClassReport() {
        $DataReportSearch = $this->input->post();
        $GroupID = $DataReportSearch['groupid'];
        $TetcherName = $DataReportSearch['tetchername'];
        if ($TetcherName != 'ВСЕ') {
            $TetcherID = $this->getTetcherInfo(['name' => $TetcherName])->id;
        } else {
            $TetcherID = -1;
        }
        $ListReport = $this->getReport($GroupID, $TetcherID);
        $this->session->set_userdata('classreport', $ListReport);
    }

    public function classes() {
        $Data = $this->getBasicData();
        $Groups = $this->getGroups();
        $Data['Groplistgeneral'] = $Groups;
        $Tetchers = $this->getTetchers();
        $Data['tetcher'] = $Tetchers;
        $DataPass = $this->session->classreport;
        if (!is_null($DataPass)) {
            for ($il = 0; $il <= count($DataPass); $il++) {
                $Vot = $this->getvoting(['workday' => $DataPass[$il]->id]);
                if (!is_null($Vot)) {
                    $NumberOfStudent = 0;
                    $CalVoting1 = 0;
                    $CalVoting2 = 0;
                    foreach ($Vot as $v) {
                        $CalVoting1 += $v->vot1;
                        $CalVoting2 += $v->vot2;
                        $NumberOfStudent++;
                    }
                    $DataPass[$il]->interest = $CalVoting1 / $NumberOfStudent;
                    $DataPass[$il]->notinter = $CalVoting2 / $NumberOfStudent;
                    $DataPass[$il]->interest = $DataPass[$il]->interest . '/' . $NumberOfStudent;
                    $DataPass[$il]->notinter = $DataPass[$il]->notinter . '/' . $NumberOfStudent;
                }
            }
            $Data['dReport'] = $DataPass;
        } else {
            $Data['dReport'] = null;
        }
        $this->session->unset_userdata('classreport');
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('classes', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function logout() {
        $GroupWorkID = $this->session->sessionIDwork;
        if ($GroupWorkID != 0 || !is_null($GroupWorkID)) {
            $this->endlesson();
        }
        $this->session->sess_destroy();
        redirect(base_url());
    }

}
