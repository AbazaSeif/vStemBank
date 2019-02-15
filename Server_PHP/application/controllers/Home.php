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
        $this->session->keep_flashdata('sessionwork');
        $this->session->keep_flashdata('sessionIDwork');
    }

    public function homepage($ID = null) {
        $Data = $this->getBasicData();
        $TetcherGroups = $this->getTetcherGroups($this->session->id);
        if (!is_null($TetcherGroups)) {
            $Data['Groplist'] = $TetcherGroups;
        } else {
            $Data['Groplist'] = null;
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
                } else {
                    $Data['GSelect'] = $ID;
                    $Data['uGroup'] = null;
                }
            } else {
                $Data['GSelect'] = $ID;
                $Data['uGroup'] = null;
            }
        } else {
            $Data['GSelect'] = $ID;
            $Data['uGroup'] = null;
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
            $this->homepage($Data['groupid']);
        }
        if ($this->setDincrimentAmount($usedid, $Amount)) {
            $this->setIncrimentAmount($this->session->id, $Amount);
            $this->setMessage('success', "Баланс обновлен");
        } else {
            $this->setMessage('error', "Баланс не обновлен");
        }
        $this->homepage($Data['groupid']);
    }

    public function startlesson() {
        $label = $this->input->post('lbl');
        $groupid = $this->input->post('gro');
        $AmountStart = $this->getSetting()->startsession;
        $this->setIncrimentAmount($this->session->id, $AmountStart);
        $GroupName = $this->getGroupsWhere(['id' => $groupid])[0]->groupname;
        $Data = [
            'groupid' => $groupid,
            'label' => $label,
            'timestart' => date("Y/m/d H:i:s"),
            'timeend' => '00:00:00',
            'status' => 1,
            'balance' => $this->getAmount($this->session->id)
        ];

        $IDG = $this->workgroups->set($Data);
        $Data['name'] = $GroupName;
        $this->session->set_flashdata('sessionIDwork', $IDG);
        $this->session->set_flashdata('sessionwork', $Data);
        $this->session->keep_flashdata('sessionwork');

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
        $GroupWorkID = $this->session->flashdata('sessionIDwork');
        $this->UpdateFactorTableForGroup($GroupWorkID);
        $this->workgroups->update($Data, ['groupid' => $groupid, 'status' => 1]);
        $this->session->set_flashdata('sessionwork', 0);
        $this->session->set_flashdata('sessionIDwork', 0);
        echo $this->getAmount($this->session->id);
    }

    public function reportpage() {
        $Data = $this->getBasicData();
        $Groups = $this->getGroups();
        $Data['Groplistgeneral'] = $Groups;
        $Tetchers = $this->getTetchers();
        $Data['tetcher'] = $Tetchers;

        $DataPass = $this->session->flashdata('groupreport');
        if (!is_null($DataPass)) {
            $Data['dReport'] = $DataPass;
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
        $GroupID = $DataReportSearch['groupid'];
        $ListReport = $this->getReport($GroupID);
        $this->session->set_flashdata('groupreport', $ListReport);
    }

    public function getClassReport() {
        $DataReportSearch = $this->input->post();
        $GroupID = $DataReportSearch['groupid'];
        $ListReport = $this->getReport($GroupID);
        $this->session->set_flashdata('classreport', $ListReport);
    }

    public function classes() {
        $Data = $this->getBasicData();
        $Groups = $this->getGroups();
        $Data['Groplistgeneral'] = $Groups;
        $Tetchers = $this->getTetchers();
        $Data['tetcher'] = $Tetchers;
        $DataPass = $this->session->flashdata('classreport');
        if (!is_null($DataPass)) {
            for ($il = 0; $il <= count($DataPass); $il++) {
                $Vot = $this->getvoting(['workday' => $DataPass[$il]->id]);
                if (!is_null($Vot)) {
                    foreach ($Vot as $v) {
                        switch ($v->vot) {
                            case 1:
                                $DataPass[$il]->interest += 1;
                                break;
                            case 2:
                                $DataPass[$il]->notinter += 1;
                                break;
                        }
                    }
                }
            }



            $Data['dReport'] = $DataPass;
        } else {
            $Data['dReport'] = null;
        }

        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('classes', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function admincode() {
        $Code = $this->input->post(NULL, TRUE);
        $AdminData = $this->getAdmins(['cardid' => $Code['code'], 'password' => $Code['pass']])[0];
        if (!is_null($AdminData)) {
            $this->session->set_userdata('admin', true);
        } else {
            $this->setMessage('error', 'У вас нет доступа');
            $this->session->set_userdata('admin', false);
        }
        redirect(base_url() . 'home');
    }

    public function adminexit() {
        $this->session->set_userdata('admin', false);
        redirect(base_url() . 'home');
    }

    public function logout() {
        $this->session->sess_destroy();
        redirect(base_url());
    }

}
