<style>
    .footer {
        position: absolute;
        bottom: 0;
        width: 100%;
        height: 60px;
    }

    .footer > .container {
        padding-right: 15px;
        padding-left: 15px;
    }
</style>
<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">Отчете по классам</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li><a href="<?php echo base_url() . 'report'; ?>">Отчете по студентам</a></li>
        <li><a href="<?php echo base_url() . 'teachers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'students'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>
<div class="tab-content">
    <div class="row">
        <?php if (!is_null($computers)): ?>
            <?php foreach ($computers as $pc): ?>
                <div class="col-lg-2">
                    <center>
                        <img src="assest/computer.png" width="100" height="100"><br>
                        <label class="label label-success"><?php echo $pc->name; ?></label><br>
                        <button onclick="poweroff(<?php echo $pc->id; ?>)" class="btn btn-xs btn-danger btn-block">Power Off</button>
                    </center>
                </div>
            <?php endforeach; ?>
        <?php endif; ?>
    </div>
</div>
<footer class="footer">
    <div class="container">
        <button onclick="closeallcomputer()" class="btn btn-lg btn-block btn-warning">Закрыть все компьютеры</button>
    </div>
</footer>
<script>
//    setInterval(location.reload(), 10000);
</script>
