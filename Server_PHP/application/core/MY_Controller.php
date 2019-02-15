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
        $this->load->library('phpserial');
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
        $Where['active'] = 0;
        $Data = $this->tgroups->get($Where);
        if (!is_null($Data)) {
            return $Data;
        } else {
            return null;
        }
    }

    public function getReport($GroupID) {
        $List = $this->workgroups->get(['groupid' => $GroupID]);
        if (!is_null($List)) {
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
        if ($AmountUser >= 0) {
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

}
