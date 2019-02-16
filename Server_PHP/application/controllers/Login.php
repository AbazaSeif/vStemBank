<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends MY_Controller {

    public function index() {
        $Data = $this->getBasicData();
        //Get Users
        $UsersList1 = $this->tusers->get(['isTecher' => 1]);
        $UsersList2 = $this->tusers->get(['isAdmin' => 1]);
        if (!is_null($UsersList1)) {
            $UsersList = array_merge($UsersList1, $UsersList2);
        } else {
            $UsersList = $UsersList2;
        }
        $Data['UList'] = $UsersList;
        $this->load->view('include/header', $Data);
        $this->load->view('login', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function loginaction() {
        $this->form_validation->set_rules('username', 'Имя пользователя', 'required', array('required' => "Пожалуйста, введите имя пользователя"));
        $this->form_validation->set_rules('password', 'пароль', 'required', array('required' => "Пожалуйста, введите пароль"));
        if ($this->form_validation->run() == FALSE) {
            $Errors = $this->form_validation->error_array();
            foreach ($Errors as $er) {
                $this->session->set_flashdata('error', $er);
            }
            redirect(site_url());
        } else {
            $cData = $this->security->xss_clean($this->input->post(NULL, TRUE));
            $DataUser = $this->tusers->get(['cardid' => $cData['username'], 'password' => $cData['password']])[0];
            if (is_null($DataUser)) {
                $this->session->set_flashdata('error', "Ошибка в имени пользователя или пароле");
                redirect(site_url());
            } else {
                foreach ($DataUser as $key => $val) {
                    $this->session->set_userdata($key, $val);
                }

                $DataCheck = $this->workgroups->get(['tetcher' => $this->session->id, 'status' => 1])[0];
                if (!is_null($DataCheck)) {
                    $GroupName = $this->getGroupsWhere(['id' => $DataCheck->groupid])[0]->groupname;
                    $this->session->set_flashdata('sessionIDwork', $DataCheck->id);
                    $DataCheck->name = $GroupName;
                    $this->session->set_flashdata('sessionwork', get_object_vars($DataCheck));
                    redirect(site_url() . 'home/' . $DataCheck->groupid);
                } else {
                    redirect(site_url() . 'home');
                }
            }
        }
    }

}
