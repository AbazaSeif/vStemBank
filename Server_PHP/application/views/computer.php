<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'report'; ?>">Отчеты</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">класс</a></li>
    <?php if ($this->session->admin): ?>
        <li><a href="<?php echo base_url() . 'tetchers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'lstudints'; ?>">Студенты</a></li>
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
