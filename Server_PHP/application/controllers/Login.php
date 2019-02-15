<?php

defined('BASEPATH') OR exit('No direct script access allowed');

class Login extends MY_Controller {

    public function index() {
        $Data = $this->getBasicData();
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
                redirect(site_url() . 'home');
            }
        }
    }

}
