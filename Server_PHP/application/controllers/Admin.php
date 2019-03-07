<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Admin
 *
 * @author Seif Abaza <Telegram @Seif_Abaza1>
 */
class Admin extends MY_Controller {

    public function __construct($config = array()) {
        parent::__construct($config);
        ini_set('max_execution_time', 0);
        ini_set('memory_limit', '2048M');
        if (!$this->session->isAdmin) {
            redirect(base_url() . 'home');
        }
    }

    public function tetchers() {
        $Data = $this->getBasicData();
        $Data['GroupList'] = $this->getGroups();
        $Tetchers = $this->getTetchers();
        if (!is_null($Tetchers)) {
            for ($i = 0; $i <= count($Tetchers); $i++) {
                $Groups = $this->getTetcherGroups($Tetchers[$i]->id);
                if (!is_null($Groups)) {
                    foreach ($Groups as $g) {
                        if (!is_null($Tetchers[$i]->groups)) {
                            $GD = $Tetchers[$i]->groups . ',' . $g->groupname;
                        } else {
                            $GD = $g->groupname;
                        }
                        $Tetchers[$i]->groups = $GD;
                    }
                }
            }
        }
        $Data['listTetchers'] = $Tetchers;
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('tetchers', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function Readcard() {
        $Data = null;
        $Path = $this->config->item('SCANFILE');
        $Loop = 0;
        while ($Loop <= 200) {
            foreach (file($Path) as $line) {
                if ((strlen($line) > 1) && (!empty($line))) {
                    $Data = $line;
                    break;
                }
            }
            if (strlen($Data) > 0) {
                break;
            }
            $Loop++;
        }
        if (is_null($Data)) {
            echo 'Карта не обнаружена';
        } else {
            echo $Data;
        }
    }

    public function uploadimage() {
        $ImageData = $this->input->post('img');
        if (!is_null($ImageData)) {
            $Filename = $this->generateRandomString(20) . '.jpg';
            $this->base64_to_jpeg($ImageData, "image_upload/$Filename");
            echo $Filename;
        } else {
            echo '';
        }
    }

    public function newtetcher() {
        $TetData = $this->input->post(NULL, TRUE);
        if (count($TetData) > 0) {
            $TData = [
                'image' => $TetData['image'],
                'name' => $TetData['name'],
                'cardid' => $TetData['name'], //$TetData['cardid'],
                'password' => $TetData['password'],
                'phonenumber' => $TetData['phone'],
                'birthdate' => $TetData['birthdate'],
                'notes1' => $TetData['note'],
                'isTecher' => 1
            ];
            $chk = $this->tusers->get(['name' => $TData['name']])[0];
            if (is_null($chk)) {
                $ID = $this->tusers->set($TData);
                foreach ($TetData['grops'] as $GroupID) {
                    $GroupData = [
                        'groupid' => $GroupID,
                        'tetcherid' => $ID
                    ];
                    $this->ttetgroups->set($GroupData);
                }

                $this->setMessage('success', 'Данные сохранены');
            } else {
                $this->setMessage('error', 'Это имя существует в базе данных');
            }
        }
        redirect(base_url() . 'teachers');
    }

    public function addtogroup() {
        $TetData = $this->input->post(NULL, TRUE);
        $ID = $TetData['userid'];
        foreach ($TetData['grops'] as $GroupID) {
            $Check = $this->getTetcherGroups($ID, $GroupID);
            if (is_null($Check)) {
                $GroupData = [
                    'groupid' => $GroupID,
                    'tetcherid' => $ID
                ];
                $this->ttetgroups->set($GroupData);
            }
        }
        $this->setMessage('success', 'Успешно');
        redirect(base_url() . 'teachers');
    }

    public function makeadmin() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isAdmin' => 1
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function makeuser() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isAdmin' => 0
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
            if ($UID == $this->session->id) {
                redirect(base_url() . 'adminout');
            }
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function makeblock() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isBlock' => 1
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function makeunblock() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isBlock' => 0
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function makedelete() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $this->tusers->delete(['id' => $UID]);
            $this->setMessage('success', 'Пользователь удален');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function ngroups() {
        $Data = $this->getBasicData();
        $Data['allgroups'] = $this->getAllGroups();
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('groups', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function closegroup() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'active' => 1
            ];
            $this->tgroups->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function opengroup() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'active' => 0
            ];
            $this->tgroups->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Успешно');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function delgroup() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $this->tgroups->delete(['id' => $UID]);
            if ($this->deleteMemberGroup($UID)) {
                $this->setMessage('success', 'Пользователь удален');
            }
        } else {
            $this->setMessage('error', 'Ошибка');
        }
    }

    public function creategroups() {
        $GrData = $this->input->post(NULL, TRUE);
        if (count($GrData) > 0) {
            $GroupData = [
                'groupname' => $GrData['gname'],
                'materials' => $GrData['gitemname'],
                'description' => $GrData['desc'],
                'active' => 0
            ];
            $this->tgroups->set($GroupData);
            $this->setMessage('success', 'Новая группа создана');
        } else {
            $this->setMessage('error', 'Ошибка');
        }
        redirect(base_url() . 'ngroups');
    }

    public function studentlist() {
        $Data = $this->getBasicData();
        $Data['GroupList'] = $this->getAllGroups();
        $lstudent = $this->getStudints();
        if (!is_null($lstudent)) {
            for ($s = 0; $s <= count($lstudent) - 1; $s++) {
                $lstudent[$s]->amount = $this->getAmount($lstudent[$s]->id);
                $Groups = $this->getStudintGroup($lstudent[$s]->id);
                if (!is_null($Groups)) {
                    foreach ($Groups as $g) {
                        if (!is_null($lstudent[$s]->groups)) {
                            $GD = $lstudent[$s]->groups . ',' . $g->groupname;
                        } else {
                            $GD = $g->groupname;
                        }
                        $lstudent[$s]->groups = $GD;
                    }
                }
            }
        }

        $Data['lstudents'] = $lstudent;
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('studints', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function recreatestudent() {
        $StudentData = $this->session->holddata;
        $chk = $this->tusers->get(['cardid' => $StudentData['cardid']])[0];
        if (!is_null($chk)) {
            $chk->cardid = '00000';
        }
        $this->tusers->update($chk, ['id' => $chk->id]);
        if (array_key_exists('userid', $StudentData)) {
            $TData = [
                'image' => $StudentData['image'],
                'name' => $StudentData['name'],
                'cardid' => $StudentData['cardid'],
                'parentname' => $StudentData['mothername'],
                'parentphone' => $StudentData['motherphone'],
                'phonenumber' => $StudentData['phone'],
                'birthdate' => $StudentData['birthdate'],
                'notes1' => $StudentData['note']
            ];
            $this->tusers->update($TData, ['id' => $StudentData['userid']]);
            $this->tusersg->delete(['userid' => $StudentData['userid']]);
            foreach ($TData['grops'] as $GroupID) {
                $this->tusersg->set(['userid' => $StudentData['userid'], 'groupid' => $GroupID]);
            }

            $this->setMessage('success', 'Данные обновлены');
        } else {
            $ID = $this->tusers->set($StudentData);
            $ParName = dirname(APPPATH);
            $Path = $ParName . DIRECTORY_SEPARATOR . 'Users' . DIRECTORY_SEPARATOR . md5($ID);
            mkdir($Path, 0777);
            $this->tusers->update(['Dirpath' => md5($ID)], ['id' => $ID]);
            foreach ($StudentData['grops'] as $GroupID) {
                $GroupData = [
                    'groupid' => $GroupID,
                    'userid' => $ID
                ];
                $this->tusersg->set($GroupData);
            }


            $this->setMessage('success', 'Данные сохранены');
        }

        redirect(base_url() . 'students');
    }

    public function createstuding() {
        $StudData = $this->input->post(NULL, TRUE);
        if (count($StudData) > 0) {

            $TData = [
                'image' => $StudData['image'],
                'name' => $StudData['name'],
                'cardid' => $StudData['cardid'],
                'parentphone' => $StudData['motherphone'],
                'parentname' => $StudData['mothername'],
                'phonenumber' => $StudData['phone'],
                'birthdate' => $StudData['birthdate'],
                'notes1' => $StudData['note'],
                'Dirpath' => '',
                'isStudent' => 1,
                'factor' => 0,
            ];
            $chk = $this->tusers->get(['cardid' => $TData['cardid']])[0];
            if (is_null($chk)) {
                $ID = $this->tusers->set($TData);
                $ParName = dirname(APPPATH);
                $Path = $ParName . DIRECTORY_SEPARATOR . 'Users' . DIRECTORY_SEPARATOR . md5($ID);
                $this->tusers->update(['Dirpath' => md5($ID)], ['id' => $ID]);
                foreach ($StudData['grops'] as $GroupID) {
                    $GroupData = [
                        'groupid' => $GroupID,
                        'userid' => $ID
                    ];
                    $this->tusersg->set($GroupData);
                }
                mkdir($Path, 0777);

                $this->setMessage('success', 'Данные сохранены');
            } else {
                $this->session->set_userdata('holddata', $TData);
                $this->setMessage('error-msg', 'Этот номер карты существует в базе данных<br>Вы хотите продолжить и отключить студента <b>' . $chk->name . '</b>?');
            }
        }
        redirect(base_url() . 'students');
    }

    public function studentaddtogroup() {
        $TetData = $this->input->post(NULL, TRUE);
        $ID = $TetData['userlistid'];
        foreach ($TetData['grops'] as $GroupID) {
            $Check = $this->getUsersInGroups($ID, $GroupID);
            if (is_null($Check)) {
                $GroupData = [
                    'groupid' => $GroupID,
                    'userid' => $ID
                ];
                $this->tusersg->set($GroupData);
            }
        }
        $this->setMessage('success', 'Успешно');
        redirect(base_url() . 'students');
    }

    public function blockstudent() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isBlock' => 1
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'Заблокирован');
        } else {
            $this->setMessage('error', 'Не удалось');
        }
    }

    public function unblockstudent() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $Update = [
                'isBlock' => 0
            ];
            $this->tusers->update($Update, ['id' => $UID]);
            $this->setMessage('success', 'обновленный');
        } else {
            $this->setMessage('error', 'Не удается найти пользователя');
        }
    }

    public function deletestudent() {
        $UID = $this->input->post('uid');
        if (!is_null($UID)) {
            $this->tusers->delete(['id' => $UID]);
            $this->tusersg->delete(['userid' => $UID]);
            $ParName = dirname(APPPATH);
            $Path = $ParName . DIRECTORY_SEPARATOR . 'Users' . DIRECTORY_SEPARATOR . md5($UID);
            rmdir($Path);
            $this->setMessage('success', 'Пользователь удален');
        } else {
            $this->setMessage('error', 'Не удается найти пользователя');
        }
    }

    public function opendirectory() {
        $UID = $this->input->post('uid');
        $ParName = dirname(APPPATH);
        $Path = $ParName . DIRECTORY_SEPARATOR . 'Users' . DIRECTORY_SEPARATOR . md5($UID);
        try {
            echo exec('explorer.exe ' . $Path);
        } catch (Exception $e) {
            echo $e->getMessage();
        }
    }

    public function StudentIncrimentAmount() {
        $Data = $this->input->post();
        $usedid = $Data['useridmoney'];
        $Amount = $Data['valueinput'];
        if (empty($Amount)) {
            $this->setMessage('error', "Баланс не обновлен");
            redirect(base_url() . 'students');
        }
        if ($this->setIncrimentAmount($usedid, $Amount)) {
            $this->setMessage('success', "Баланс обновлен");
        } else {
            $this->setMessage('error', "Баланс не обновлен");
        }
        redirect(base_url() . 'students');
    }

    public function StudentDincrimentAmount() {
        $Data = $this->input->post();
        $usedid = $Data['useridmoneyd'];
        $Amount = $Data['valueinput'];
        if (empty($Amount)) {
            $this->setMessage('error', "Баланс не обновлен");
            redirect(base_url() . 'students');
        }
        if ($this->setDincrimentAmount($usedid, $Amount)) {
            $this->setMessage('success', "Баланс обновлен");
        } else {
            $this->setMessage('error', "Баланс не обновлен");
        }
        redirect(base_url() . 'students');
    }

    public function edituser($UserID) {
        if (!is_null($UserID)) {
            $Data = $this->getBasicData();
            $Data['GroupList'] = $this->getGroups();
            $UserData = $this->tusers->get(['id' => $UserID])[0];
            $Data['uData'] = $UserData;

            $this->load->view('include/header', $Data);
            $this->load->view('include/basic_header', $Data);
            $this->load->view('tetchersedit', $Data);
            $this->load->view('include/footer', $Data);
        } else {
            redirect(base_url() . 'teachers');
        }
    }

    public function dataupdate() {
        $TetData = $this->input->post(NULL, TRUE);
        if (count($TetData) > 0) {
            $TData = [
                'image' => $TetData['image'],
                'name' => $TetData['name'],
                'cardid' => $TetData['name'],
                'password' => $TetData['password'],
                'phonenumber' => $TetData['phone'],
                'birthdate' => $TetData['birthdate'],
                'notes1' => $TetData['note']
            ];

            $this->tusers->update($TData, ['id' => $TetData['userid']]);
            $this->ttetgroups->delete(['tetcherid' => $TetData['userid']]);
            foreach ($TetData['grops'] as $GroupID) {
                $this->ttetgroups->set(['tetcherid' => $TetData['userid'], 'groupid' => $GroupID]);
            }

            $this->setMessage('success', 'Данные обновлены');
        }
        redirect(base_url() . 'teachers');
    }

    public function studeintsupdate() {
        $TetData = $this->input->post(NULL, TRUE);
        if (count($TetData) > 0) {
            $TData = [
                'image' => $TetData['image'],
                'name' => $TetData['name'],
                'cardid' => $TetData['cardid'],
                'parentname' => $TetData['mothername'],
                'parentphone' => $TetData['motherphone'],
                'phonenumber' => $TetData['phone'],
                'birthdate' => $TetData['birthdate'],
                'notes1' => $TetData['note']
            ];
            $chk = $this->tusers->get(['cardid' => $TData['cardid']])[0];
            if (is_null($chk)) {
                $this->tusers->update($TData, ['id' => $TetData['userid']]);
                $this->tusersg->delete(['userid' => $TetData['userid']]);
                foreach ($TetData['grops'] as $GroupID) {
                    $this->tusersg->set(['userid' => $TetData['userid'], 'groupid' => $GroupID]);
                }
                $this->setMessage('success', 'Данные обновлены');
            } else {
                if ($chk->id == $TetData['userid']) {
                    unset($TData['cardid']);
                    $this->tusers->update($TData, ['id' => $TetData['userid']]);
                    $this->tusersg->delete(['userid' => $TetData['userid']]);
                    foreach ($TetData['grops'] as $GroupID) {
                        $this->tusersg->set(['userid' => $TetData['userid'], 'groupid' => $GroupID]);
                    }
                    $this->setMessage('success', 'Данные обновлены');
                } else {
                    $this->session->set_userdata('holddata', $TetData);
                    $this->setMessage('error-msg', 'Этот номер карты существует в базе данных<br>Вы хотите продолжить и отключить студента <b>' . $chk->name . '</b>?');
                }
            }
        }
        redirect(base_url() . 'students');
    }

    public function upgradegroup() {
        $GrData = $this->input->post(NULL, TRUE);
        if (count($GrData) > 0) {
            $GroupData = [
                'groupname' => $GrData['gname'],
                'materials' => $GrData['gitemname'],
                'description' => $GrData['desc']
            ];
            $this->tgroups->update($GroupData, ['id' => $GrData['gid']]);
            $this->setMessage('success', 'Новая группа создана');
        } else {
            $this->setMessage('error', 'Нет ввод находки');
        }
        redirect(base_url() . 'ngroups');
    }

    public function getTecherInformations() {
        $ID = $this->input->post('tetinfo');
        $Data = $this->getTetcherInfo(['id' => $ID]);
        if (!is_null($Data)) {
            $GData = $this->getTetcherGroups($Data->id);
            if (!is_null($GData)) {
                $Data->group = json_encode($GData);
            } else {
                $Data->group = "";
            }
            echo json_encode($Data);
        } else {
            echo '';
        }
    }

    public function getStudentsInformations() {
        $ID = $this->input->post('stinfo');
        $Data = $this->getStudintInfo(['id' => $ID]);
        if (!is_null($Data)) {
            $GData = $this->getStudintGroup($Data->id);
            if (!is_null($GData)) {
                $Data->group = json_encode($GData);
            } else {
                $Data->group = json_encode(array());
            }
            echo json_encode($Data);
        } else {
            echo '';
        }
    }

    public function getGroupInformations() {
        $ID = $this->input->post('groinfo');
        $Groupdata = $this->getAllGroupsWhere(['id' => $ID])[0];
        if (!is_null($Groupdata)) {
            echo json_encode($Groupdata);
        } else {
            echo '';
        }
    }

}
