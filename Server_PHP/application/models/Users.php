<?php

defined('BASEPATH') OR exit('No direct script access allowed');

/**
 * Description of Table_faq
 *
 * @author alienware
 */
class Users extends CI_Model {

    private $TableName = 'users';

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

    public function getAnd($where1, $where2) {
        $this->db->where($where1[0], $where1[1]);
        $this->db->where($where2[0], $where2[1]);
        $querey = $this->db->get($this->TableName);
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
