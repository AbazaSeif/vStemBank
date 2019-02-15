<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'report'; ?>">Отчеты</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">класс</a></li>
    <?php if ($this->session->admin): ?>
        <li><a href="<?php echo base_url() . 'tetchers'; ?>">Преподаватели</a></li>
        <li class="active"><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'lstudints'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>

<div class="tab-content">
    <div id="home" class="tab-pane fade in active">
        <h3>Группы</h3>
        <div class="well">
            <div class="row">
                <form action="<?php echo base_url() . 'creategroup'; ?>" method="POST">      
                    <div class="col-sm-4">
                        <label for="name">Название группы</label>
                        <input type="text" required name="gname" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="name">Название предмета</label>
                        <input type="text" required name="gitemname" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="note">Описание</label>
                        <input type="text" name="desc" class="form-control input-group">
                    </div>
            </div>
            <br>
            <hr>
            <div style="padding-left: 10%;padding-right: 10%;">
                <input type="hidden" name="image" id="imagepath" value="">
                <button type="submit" class="btn btn-success btn-block">Создать</button>
            </div>
            </form>
        </div>
        <hr>
        <table id="tablestud" class="table table-condensed">
            <thead>
                <tr>
                    <th>№</th>
                    <th>Название группы</th>
                    <th>Название предмета</th>
                    <th>Описание</th>
                    <th>Завершен</th>
                    <th><center>действие</center></th>
            </tr>
            </thead>
            <tbody>
                <?php if (!is_null($allgroups)): ?>
                    <?php foreach ($allgroups as $gUser): $index = 1; ?>
                        <tr class="<?php echo ($gUser->active == 1 ? 'bg-danger' : 'bg-success'); ?>">
                            <td><?php echo $index; ?></td>
                            <td><?php echo $gUser->groupname; ?><?php echo ($gUser->isAdmin ? '(*)' : ''); ?></td>
                            <td><?php echo $gUser->materials; ?></td>
                            <td><?php echo $gUser->description; ?></td>
                            <td><?php echo ($gUser->active == 1 ? 'Да' : 'Нет'); ?></td>
                            <td><center>
                        <?php if ($gUser->active == 0): ?>
                            <a onclick="gisdone(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-success">завершен</a>
                        <?php else: ?>
                            <a onclick="gnotdone(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-success">Не завершен</a>
                        <?php endif; ?>
                        <a onclick="gdelet(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-danger">Удалить</a>
                    </center></td>
                    </tr>
                    <?php
                    $index++;
                endforeach;
                ?>
            <?php endif; ?>
            </tbody>
        </table>
    </div>
</div>

<div class="modal fade" id="scaning" role="dialog">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Сканирование карты ....</h4>
            </div>
            <div class="modal-body">
                <p>Пожалуйста, положите карту на ридере</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

