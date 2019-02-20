<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'report'; ?>">Отчеты</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">класс</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li><a href="<?php echo base_url() . 'teachers'; ?>">Преподаватели</a></li>
        <li class="active"><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'students'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>

<div class="tab-content">
    <div id="home" class="tab-pane fade in active">
        <h3>Группы</h3>
        <div class="well">
            <div class="row">
                <form id="groupform" action="<?php echo base_url() . 'creategroup'; ?>" method="POST">      
                    <div class="col-sm-4">
                        <label for="name">Название группы</label>
                        <input type="text" id="gname" required name="gname" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="name">Название предмета</label>
                        <input type="text" id="gitemname" name="gitemname" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="note">Описание</label>
                        <input type="text" id="desc" name="desc" class="form-control input-group">
                    </div>
            </div>
            <br>
            <hr>
            <input type="hidden" name="gid" id="gid">
            <div class="row" style="padding-left: 10%;padding-right: 10%;">
                <div class="col-sm-6">
                    <a id="cancelbtn" style="display: none;" class="btn btn-danger btn-block">Очистить все поля</a>
                </div>
                <div class="col-sm-6">
                    <button type="submit" id="actionbtn" class="btn btn-success btn-block">Создать</button>    
                </div>
            </div>
            </form>
        </div>
        <hr>
        <table class="table table-bordered" id="dataTablegroups" width="100%" cellspacing="0">
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
                <?php if (!is_null($allgroups)): $index = 1; ?>

                    <?php foreach ($allgroups as $gUser): ?>
                        <tr id="<?php echo $gUser->id; ?>">
                            <td><?php echo $index++; ?></td>
                            <td><?php echo ($gUser->active == 1 ? '<strike>' . $gUser->groupname . '</strike>' : $gUser->groupname); ?></td>
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

