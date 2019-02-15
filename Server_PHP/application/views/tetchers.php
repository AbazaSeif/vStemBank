<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'report'; ?>">Отчеты</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">класс</a></li>
    <?php if ($this->session->admin): ?>
        <li class="active"><a href="<?php echo base_url() . 'tetchers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'lstudints'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>

<div class="tab-content">
    <div id="home" class="tab-pane fade in active">
        <h3>Преподаватели</h3>
        <div class="well">
            <div class="row">
                <div class="col-sm-1">
                    <center>
                        <form id="form1" runat="server">
                            <img src="assest/user.png" id="blah" width="120" height="140">
                            <label class="btn btn-default btn-block btn-file">
                                Browse <input id="imgInp" name="userfile" type="file" style="display: none;">
                            </label>
                        </form>
                    </center>
                </div>
                <form action="<?php echo base_url() . 'datasave'; ?>" method="POST" enctype="multipart/form-data">      
                    <div class="col-sm-4">
                        <label for="name">ФИО</label>
                        <input type="text" required name="name" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="cardid">Карта</label>
                        <a href="" onclick="scancard()" data-toggle="modal" data-target="#scaning" class="btn btn-xs btn-danger">Scan</a>
                        <input type="text" readonly required name="cardid" id="cardid" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="birthdate">Дата рождения</label>
                        <input type="date" required name="birthdate" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="grorepo">Группы:</label>
                        <select size="3" required multiple="multiple" tabindex="1" class="form-control" name="grops[]" id="grops">
                            <?php if (!is_null($GroupList)): ?>
                                <?php foreach ($GroupList as $group): ?>
                                    <option value="<?php echo $group->id; ?>"><?php echo $group->groupname; ?></option>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </select>
                    </div>
                    <div class="col-sm-4">
                        <label for="phone">Телефон</label>
                        <input required type="tel" name="phone" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="note">Комментарий</label>
                        <input type="text" name="note" class="form-control input-group">
                    </div>
                    <div style="padding-left: 9%;" class="col-sm-9">
                        <label for="note">пароль</label>
                        <input required type="text" name="password" class="form-control input-group">
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
                    <th>ФИО</th>
                    <th>Телефон</th>
                    <th>Группа</th>
                    <th>Комментарий</th>
                    <th><center>действие</center></th>
            </tr>
            </thead>
            <tbody>
                <?php if (!is_null($listTetchers)): $index = 1; ?>
                    <?php foreach ($listTetchers as $gUser): ?>
                        <tr class="<?php echo ($gUser->isBlock ? 'bg-danger' : 'bg-success'); ?>">
                            <td><?php echo $index++; ?></td>
                            <td><?php echo $gUser->name; ?><?php echo ($gUser->isAdmin ? '(*)' : ''); ?></td>
                            <td><?php echo $gUser->phonenumber; ?></td>
                            <td><?php echo $gUser->groups; ?></td>
                            <td><?php echo $gUser->note1; ?></td>
                            <td><center>
                        <?php if ($gUser->isAdmin): ?>
                            <a onclick="unadmin(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-success">пользователь</a>
                        <?php else: ?>
                            <a onclick="admin(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-success">Администратор</a>
                        <?php endif; ?>
                        <?php if ($gUser->isBlock): ?>
                            <a onclick="unblock(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-warning">открыть</a>
                        <?php else: ?>
                            <?php if ($gUser->id != $this->session->id): ?>
                                <a onclick="block(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-warning">Заблокирован</a>
                            <?php else: ?>
                                <a class="btn btn-xs btn-warning disabled">Заблокирован</a>
                            <?php endif; ?>
                        <?php endif; ?>
                        <?php if ($gUser->id != $this->session->id): ?>
                            <a onclick="delet(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-danger">Удалить</a>
                        <?php else: ?>
                            <a class="btn btn-xs btn-danger disabled">Удалить</a>
                        <?php endif; ?>
                        <a onclick="addtogroup(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-info">Добавить в группу</a>
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
<div class="modal fade" id="addingroup" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Добавить в группу</h4>
            </div>
            <form action="<?php echo base_url() . 'addtogroup'; ?>" method="POST">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-12">
                            <label for="grorepo">Группы:</label>
                            <select size="3" required multiple="multiple" tabindex="1" class="form-control" name="grops[]" id="grops">
                                <?php if (!is_null($GroupList)): ?>
                                    <?php foreach ($GroupList as $group): ?>
                                        <option value="<?php echo $group->id; ?>"><?php echo $group->groupname; ?></option>
                                    <?php endforeach; ?>
                                <?php endif; ?>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <input type='hidden' name="userid" id="userid" value="">
                    <button type="submit" class="btn btn-success">добавлять</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                </div>
            </form>
        </div>
    </div>
</div>
