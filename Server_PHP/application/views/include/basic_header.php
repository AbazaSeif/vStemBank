<h2>Преподаватель : <?php echo $username; ?></h2>
<div class="row">
    <div class="col-sm-8"></div>
    <?php if ($this->session->isAdmin): ?>
        <?php if (!$this->session->admin): ?>
            <div class="col-sm-1"><button data-toggle="modal" data-target="#admincode" class="btn btn-xs btn-danger btn-block">админ</button></div>
        <?php else: ?>
            <div class="col-sm-1"><a href="<?php echo base_url() . 'adminout'; ?>" class="btn btn-xs btn-danger btn-block">админ выйти</a></div>
        <?php endif; ?>
    <?php endif; ?>
    <div class="col-sm-1"><a href="<?php echo base_url() . 'exit'; ?>" class="btn btn-xs btn-success btn-block">выйти</a></div>
    <div class="col-sm-2"><a href="<?php echo base_url() . 'computers'; ?>" class="btn btn-xs btn-warning btn-block">Выключение компьютера</a></div>
</div>
<br>
<div class="row">
    <div class="col-sm-9"></div>
    <div class="col-sm-1"><strong>Time : <i id="timerler">00:00:00</i></strong></div>
    <div class="col-sm-2">Доступный баланс: <strong id="amoutn"><?php echo $sesuseramount; ?> </strong>STC</div>
</div>
<hr>
<?php $this->load->view('include/messages'); ?>
