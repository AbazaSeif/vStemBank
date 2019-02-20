<h2>Преподаватель : <?php echo $username; ?></h2>
<div class="row">
    <div class="col-sm-8"></div>
    <div class="col-sm-1"><a href="<?php echo base_url() . 'exit'; ?>" class="btn btn-xs btn-success btn-block">выйти</a></div>
    <div class="col-sm-2"><a href="<?php echo base_url() . 'computers'; ?>" class="btn btn-xs btn-warning btn-block">Выключение компьютера</a></div>
</div>
<br>
<div class="row">
    <div class="col-sm-8"></div>
    <div class="col-sm-2"><strong>Время : <i id="timerler">00:00:00</i></strong></div>
    <div class="col-sm-2">Доступный баланс: <strong id="amoutn"><?php echo $sesuseramount; ?> </strong>STC</div>
</div>
<hr>
<?php $this->load->view('include/messages'); ?>
