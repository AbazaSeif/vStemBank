<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Settings
 *
 * @author Seif Abaza <Telegram @Seif_Abaza1>
 */
class Settings extends MY_Controller {

    public function __construct($config = array()) {
        parent::__construct($config);
    }

    public function view() {
        $Data = $this->getBasicData();
        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('setting', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function save() {
        $SData = $this->input->post(NULL, TRUE);
        if (!is_null($SData)) {
            $SData['chargebutton'] = implode(",", $SData['chargebutton']);
            $this->setting->set($SData);
            $this->setMessage('success', 'Обновление системы');
            redirect(base_url() . 'setting');
        } else {
            $this->setMessage('error', 'Система не обновляется');
            redirect(base_url() . 'setting');
        }
    }

    public function update() {
        $SData = $this->input->post(NULL, TRUE);
        if (!is_null($SData)) {
            $DataSetting = $this->setting->get(['id' => 1])[0];
            if (is_null($DataSetting)) {
                $this->save();
            }
            $SData['chargebutton'] = implode(",", $SData['chargebutton']);
            $this->setting->update($SData, ['id' => 1]);
            $this->setMessage('success', 'Обновление системы');
            redirect(base_url() . 'setting');
        } else {
            $this->setMessage('error', 'Система не обновляется');
            redirect(base_url() . 'setting');
        }
    }

    public function ComputerPage() {
        $Data = $this->getBasicData();
        $Data['computers'] = $this->GetOnlineComputers();

        $this->load->view('include/header', $Data);
        $this->load->view('include/basic_header', $Data);
        $this->load->view('computer', $Data);
        $this->load->view('include/footer', $Data);
    }

    public function Computeraction() {
        $PC = $this->input->post('pc');
        if (!is_null($PC)) {
            $this->TurnOfComputer($PC);
        }
    }

    public function allComputeraction() {
        $PC = $this->GetOnlineComputers();
        if (!is_null($PC)) {
            foreach ($PC as $sPC) {
                $this->TurnOfComputer($sPC->id);
            }
        }
    }

}
