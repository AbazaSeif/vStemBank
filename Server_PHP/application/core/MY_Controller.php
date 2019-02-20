<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of MY_Controller
 *
 * @author Seif Abaza <Telegram @Seif_Abaza1>
 */
class MY_Controller extends CI_Controller {

    public function __construct($config = array()) {
        parent::__construct($config);
        $this->load->library('form_validation');
        $this->load->library('session');
        $this->load->helper('url');

        $this->load->model('Setting', 'setting', TRUE);
        $this->load->model('Users', 'tusers', TRUE);
        $this->load->model('Usergroup', 'tusersg', TRUE);
        $this->load->model('Groups', 'tgroups', TRUE);
        $this->load->model('Tetchersgroup', 'ttetgroups', TRUE);
        $this->load->model('Workgroups', 'workgroups', TRUE);
        $this->load->model('Existusers', 'existusers', TRUE);
        $this->load->model('Balance', 'tbalance', TRUE);
        $this->load->model('Voting', 'tvoting', TRUE);
        $this->load->model('Computercontrol', 'tcomputer', TRUE);
    }

    public function getBasicData() {

        if (!is_null($this->session->id)) {
            $UserData = [
                'username' => $this->session->name,
                'phone' => $this->session->phonenumber,
                'birthdate' => $this->session->birthdate,
                'notes' => $this->session->notes1,
                'isBlock' => $this->session->isBlock,
                'isTecher' => $this->session->isTecher,
                'isAdmin' => $this->session->isAdmin,
                'lastlogin' => $this->session->lastlogin,
                'sesuseramount' => $this->getAmount($this->session->id)
            ];
        }
        $Data = [
            'welcome' => $this->getSetting()->name,
            'basepath' => base_url(),
            'settings' => $this->getSetting()
        ];
        if (count($UserData) > 0) {
            $Data = array_merge($UserData, $Data);
        }

        return $Data;
    }

    public function TurnOfComputer($ID) {
        $Data = [
            'mode' => 0
        ];
        $this->tcomputer->update($Data, ['id' => $ID]);
    }

    public function GetOnlineComputers() {
        $Data = $this->tcomputer->get(['mode' => 1]);
        return $Data;
    }

    public function getvoting($Where) {
        $Data = $this->tvoting->get($Where);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function setMessage($type, $message) {
        $this->session->set_flashdata($type, $message);
    }

    public function getSetting() {
        $Data = $this->setting->get()[0];
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getGroups() {
        $Data = $this->tgroups->get(['active' => 0]);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function deleteMemberGroup($ID) {
        if (!is_null($ID)) {
            $this->tusersg->delete(['groupid' => $ID]);
            $this->ttetgroups->delete(['groupid' => $ID]);
            return true;
        } else {
            return false;
        }
    }

    public function getAllGroups() {
        $Data = $this->tgroups->get();
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getGroupsWhere($Where) {
        if (!array_key_exists('active', $Where)) {
            $Where['active'] = 0;
        }
        $Data = $this->tgroups->get($Where);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }
    
    public function getAllGroupsWhere($Where) {
        $Data = $this->tgroups->get($Where);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getReportOfThisStudent($StudintID, $GroupID) {
        $FinalReport = array();
        $Report = array();
        if ($GroupID != -1) {
            $GroupWork = $this->workgroups->get(['groupid' => $GroupID]);
        } else {
            $GroupWork = $this->workgroups->get();
        }
        foreach ($GroupWork as $GW) {
            if ($StudintID != -1) {
                $StudentList = $this->existusers->get(['workinggroupid' => $GW->id, 'groupid' => $GW->groupid, 'userid' => $StudintID])[0];
                if (!is_null($StudentList)) {
                    $Wallet = $this->getAmount($StudentList->userid);
                } else {
                    $Wallet = 0;
                }
                if (!is_null($StudentList)) {
                    $Report['groupid'] = $GW->groupid;
                    $Report['Exist'] = true;
                    $Report['LoginTime'] = $StudentList->timelogin;
                    $Report['TimeEnd'] = $GW->timeend;
                    $Report['Label'] = $GW->label;
                    $Report['isDelayed'] = ($StudentList->delay == 1 ? true : false);
                    $Report['Balance'] = $Wallet;
                } else {
                    $Report['groupid'] = $GW->groupid;
                    $Report['Exist'] = false;
                    $Report['LoginTime'] = $GW->timestart;
                    $Report['TimeEnd'] = $GW->timeend;
                    $Report['Label'] = $GW->label;
                    $Report['isDelayed'] = true;
                    $Report['Balance'] = $Wallet;
                }
                array_push($FinalReport, $Report);
            } else {
                $StudentList = $this->existusers->get(['workinggroupid' => $GW->id, 'groupid' => $GW->groupid]);
                foreach ($StudentList as $SList) {
                    $Wallet = $this->getAmount($SList->userid);
                    $Report['groupid'] = $GW->groupid;
                    $Report['Exist'] = true;
                    $Report['LoginTime'] = $SList->timelogin;
                    $Report['TimeEnd'] = $GW->timeend;
                    $Report['Label'] = $GW->label;
                    $Report['isDelayed'] = ($SList->delay == 1 ? true : false);
                    $Report['Balance'] = $Wallet;
                    array_push($FinalReport, $Report);
                }
            }
        }
        return $FinalReport;
    }

    public function getReport($GroupID, $TetcherID) {
        if (($TetcherID == -1) && ($GroupID != -1)) {
            $List = $this->workgroups->get(['groupid' => $GroupID]);
        } elseif (($TetcherID != -1) && ($GroupID == -1)) {
            $List = $this->workgroups->get(['tetcher' => $TetcherID]);
        } elseif (($TetcherID != -1) && ($GroupID != -1)) {
            $List = $this->workgroups->get(['tetcher' => $TetcherID, 'groupid' => $GroupID]);
        } elseif (($TetcherID == -1) && ($GroupID == -1)) {
            $List = $this->workgroups->get();
        }
        if (!is_null($List)) {
            for ($i = 0; $i <= count($List) - 1; $i++) {
                $Voting = $this->tvoting->get(['workday' => $List[$i]->id]);
                if (!is_null($Voting)) {
                    foreach ($Voting as $mVote) {
                        switch ($mVote->vot1) {
                            case 1:
                                $List[$i]->isithard += 0;
                                break;
                            case 2:
                                $List[$i]->isithard += 0.2;
                                break;
                            case 3:
                                $List[$i]->isithard += 0.4;
                                break;
                            case 4:
                                $List[$i]->isithard += 0.8;
                                break;
                            case 5:
                                $List[$i]->isithard += 1;
                                break;
                        }
                        switch ($mVote->vot2) {
                            case 1:
                                $List[$i]->por += 0;
                                break;
                            case 2:
                                $List[$i]->por += 0.2;
                                break;
                            case 3:
                                $List[$i]->por += 0.4;
                                break;
                            case 4:
                                $List[$i]->por += 0.8;
                                break;
                            case 5:
                                $List[$i]->por += 1;
                                break;
                        }
                    }
                }
            }
            return $List;
        } else {
            return null;
        }
    }

    public function getStudints() {
        $Data = $this->tusers->get(['isStudent' => 1]);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getStudintGroup($StudintID) {
        $Data = $this->tusersg->get(['userid' => $StudintID]);
        if (!is_null($Data)) {
            $index = 0;
            $GArrayUser = array();
            foreach ($Data as $groups) {
                $GData = $this->getGroupsWhere(['id' => $groups->groupid])[0];
                if (!is_null($GData)) {
                    $GArrayUser[$index++] = $GData;
                }
            }
            return $GArrayUser;
        } else {
            return null;
        }
    }

    public function getStudintInfo($Where) {
        $Where['isStudent'] = 1;
        $Data = $this->tusers->get($Where);
        if (!is_null($Data)) {
            return $Data[0];
        } else {
            return null;
        }
    }

    public function getTetchers() {
        $Data = $this->tusers->get(['isTecher' => 1]);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getTetcherInfo($Where) {
        $Where['isTecher'] = 1;
        $Data = $this->tusers->get($Where);
        if (!is_null($Data)) {
            return $Data[0];
        } else {
            return null;
        }
    }

    public function getTetcherGroups($TetcherID, $Groupid = null) {
        if (is_null($Groupid)) {
            $TeGData = $this->ttetgroups->get(['tetcherid' => $TetcherID]);
        } else {
            $TeGData = $this->ttetgroups->get(['tetcherid' => $TetcherID, 'groupid' => $Groupid]);
        }
        if (!is_null($TeGData)) {
            $List = array();
            $index = 0;
            foreach ($TeGData as $TEG) {
                $Data = $this->tgroups->get(['id' => $TEG->groupid, 'active' => 0])[0];
                if (!is_null($Data)) {
                    $List[$index++] = $Data;
                }
            }
            return $List;
        } else {
            return null;
        }
    }

    public function getAdmins($WhereCode) {
        $WhereCode['isAdmin'] = 1;
        $Data = $this->tusers->get($WhereCode);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getUserGroup($User) {
        if (is_numeric($User)) {
            $Where = ['userid' => $User];
        } else {
            $DataUser = $this->getStudintInfo(['name' => $User]);
            $Where = ['userid' => $DataUser->id];
        }
        $Data = $this->tusersg->get($Where);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getUsersInGroups($StudentID, $Groupid = null) {
        if (is_null($Groupid)) {
            $TeGData = $this->tusersg->get(['userid' => $StudentID]);
        } else {
            $TeGData = $this->tusersg->get(['userid' => $StudentID, 'groupid' => $Groupid]);
        }
        if (!is_null($TeGData)) {
            $List = array();
            $index = 0;
            foreach ($TeGData as $TEG) {
                $Data = $this->tgroups->get(['id' => $TEG->groupid, 'active' => 0])[0];
                if (!is_null($Data)) {
                    $List[$index++] = $Data;
                }
            }
            return $List;
        } else {
            return null;
        }
    }

    public function getUsersInGroup($Group_ID) {
        $Data = $this->tusersg->get(['groupid' => $Group_ID]);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getAmount($UserID) {
        $Amount = $this->tbalance->get(['userid' => $UserID])[0];
        if (is_null($Amount)) {
            $this->tbalance->set(['userid' => $UserID, 'amount' => 0]);
            return 0;
        } else {
            return intval($Amount->amount);
        }
    }

    public function UpdateAmount($UserID, $Amount) {
        if ($Amount >= 0) {
            $this->tbalance->update(['amount' => intval($Amount)], ['userid' => intval($UserID)]);
        }
    }

    public function setIncrimentAmount($UserID, $Amount) {
        $AmountUser = $this->getAmount($UserID);
        if ($Amount > 0) {
            $AmountUser += $Amount;
            $this->UpdateAmount($UserID, $AmountUser);
            return true;
        } else {
            return false;
        }
    }

    public function setResetAmount($UserID) {
        $this->UpdateAmount($UserID, 0);
    }

    public function setDincrimentAmount($UserID, $Amount) {
        $AmountUser = $this->getAmount($UserID);
        if ($AmountUser >= $Amount) {
            $AmountUser -= $Amount;
            $this->UpdateAmount($UserID, $AmountUser);
            return true;
        } else {
            return false;
        }
    }

    public function base64_to_jpeg($base64_string, $output_file) {
        $ifp = fopen($output_file, 'w+');
        $data = explode(',', $base64_string);
        fwrite($ifp, base64_decode($data[1]));
        fclose($ifp);
        return $output_file;
    }

    public function generateRandomString($length = 10) {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }

    public function UpdateFactorTableForGroup($GroupWorkID) {
        $Data = $this->existusers->get(['workinggroupid' => $GroupWorkID]);
        if (!is_null($Data)) {
            foreach ($Data as $Student) {
                if ($Student->delay == 1) {
                    $SD = $this->getStudintInfo(['id' => $Student->userid]);
                    $Factor = $SD->factor + 1;
                    $this->tusers->update(['factor' => $Factor], ['id' => $Student->userid, 'isStudent' => 1]);
                }
            }
        }
    }

    public function dateDifference($date1, $date2) {
        $date1 = strtotime($date1);
        $date2 = strtotime($date2);
        $diff = abs($date1 - $date2);

        $day = $diff / (60 * 60 * 24); // in day
        $dayFix = floor($day);
        $dayPen = $day - $dayFix;
        if ($dayPen > 0) {
            $hour = $dayPen * (24); // in hour (1 day = 24 hour)
            $hourFix = floor($hour);
            $hourPen = $hour - $hourFix;
            if ($hourPen > 0) {
                $min = $hourPen * (60); // in hour (1 hour = 60 min)
                $minFix = floor($min);
                $minPen = $min - $minFix;
                if ($minPen > 0) {
                    $sec = $minPen * (60); // in sec (1 min = 60 sec)
                    $secFix = floor($sec);
                }
            }
        }
        $str = "";
        if ($dayFix > 0)
            $str .= $dayFix . " день ";
        if ($hourFix > 0)
            $str .= $hourFix . " час ";
        if ($minFix > 0)
            $str .= $minFix . " мин ";
        if ($secFix > 0)
            $str .= $secFix . " сек ";
        return $str;
    }

}
