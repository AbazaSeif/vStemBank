<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Description of Groups
 *
 * @author Seif Abaza <Telegram @Seif_Abaza1>
 */
class Voting extends CI_Model {

    private $TableName = 'voting';

    public function __construct() {
        parent::__construct();
    }

    public function get($where = null) {
        if (is_null($where)) {
            $querey = $this->db->get($this->TableName);
        } else {
            $querey = $this->db->get_where($this->TableName, $where);
        }

        if ($querey->result()) {
            return $querey->result();
        } else {
            return null;
        }
    }

    public function set($data) {
        $this->db->insert($this->TableName, $data);
        $insert_id = $this->db->insert_id();
        return $insert_id;
    }

    public function update($data, $where) {
        $this->db->where($where);
        return $this->db->update($this->TableName, $data);
    }

    public function delete($where) {
        $this->db->delete($this->TableName, $where);
    }

}
