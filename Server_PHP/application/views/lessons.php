<ul class="nav nav-tabs">
    <li class="active"><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
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
    <div id="home" class="tab-pane fade in active">
        <h3>Урок</h3>
        <div class="well">
            <div class="row">
                <div class="col-sm-5">
                    <div class="form-group">
                        <label for="sel1">Выбрать группу:</label>
                        <?php if ($this->session->sessionwork == 0): ?>
                            <?php
                            if (!is_null($Groplist)):
                                rsort($Groplist);
                                ?>
                                <select onclick="selectgroup()" class="form-control" id="group">
                                    <?php if (!is_null($Groplist)): ?>
                                        <?php foreach ($Groplist as $group): ?>
                                            <option value="<?php echo $group->id; ?>" <?php echo ($GSelect != null ? ($GSelect == $group->id ? 'selected' : '') : ''); ?>><?php echo $group->groupname; ?></option>
                                        <?php endforeach; ?>
                                    <?php endif; ?>
                                </select>
                            <?php else: ?>
                                <input type="text" class="form-control input-group" disabled value="Нет группы найти">
                            <?php endif; ?>
                            <?php
                        else:
                            ?>
                            <input type="text" class="form-control input-group" value="<?php echo $this->session->sessionwork['name']; ?>" disabled>
                        <?php endif; ?>
                    </div>
                </div>
                <div class="col-sm-5">
                    <div class="form-group">
                        <label for="sel1">Тема урока:</label>
                        <?php if ($cStart == true): ?>
                            <?php if ($this->session->sessionwork == 0): ?>
                                <input type="text" class="form-control input-group" id="labellesson" value="">
                            <?php else: ?>
                                <input type="text" class="form-control input-group" disabled value="<?php echo $this->session->sessionwork['label']; ?>">
                            <?php endif; ?>
                        <?php else: ?>
                            <input type="text" class="form-control input-group" disabled value="">
                        <?php endif; ?>
                    </div>
                </div>
                <br>
                <div class="col-sm-2">
                    <?php if ($cStart == true): ?>
                        <input type="hidden" id="groupidlesson" value="<?php echo (is_null($GSelect) ? $this->session->sessionwork['groupid'] : $GSelect); ?>">
                        <?php if ($this->session->sessionwork == 0): ?>
                            <button type="button" id="btnstarttot" onclick="startlesson()" class="btn btn-block btn-lg btn-success">Начните Урок</button>
                            <?php
                        else:
                            ?>
                            <button type="button" id="btnstarttot" onclick="startlesson()" class="btn btn-block btn-lg btn-danger">заверщить урок</button>
                        <?php endif; ?>
                    <?php else: ?>
                        <button type="button" class="btn btn-block btn-lg btn-success disabled">Начните Урок</button>
                    <?php endif; ?>
                </div>
            </div>
        </div>
        <hr>
        <table class="table table-bordered" id="dataTablelesson" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>№</th>
                    <th>ФИО</th>
                    <th>Баланс</th>
                    <th>Множитель</th>
                    <th>Время входа</th>
                    <th>Блокировка</th>
                    <th><center>Зачислить</center></th>
            <th><center>Списать</center></th>
            </tr>
            </thead>
            <tbody>
                <?php if (!is_null($uGroup)):$index = 1; ?>
                    <?php foreach ($uGroup as $gUser): ?>
                        <tr class="<?php echo (intval($gUser->online) == 0 ? 'bg-danger' : 'bg-success'); ?>">
                            <td><?php echo $index; ?></td>
                            <td><?php echo $gUser->name; ?></td>
                            <td><?php echo $gUser->amount; ?></td>
                            <td><?php echo $gUser->factor; ?></td>
                            <td><?php echo $gUser->lastlogin; ?></td>
                            <td><?php echo ($gUser->isBlock ? 'Да' : 'Нет'); ?></td>
                            <td><center><a class="btn btn-xs btn-success <?php echo (intval($gUser->online) == 0 ? 'disabled' : ''); ?>" onclick="showAmountInc('<?php echo $gUser->id; ?>', '<?php echo $gUser->name; ?>')" style="width: 100px">+</a></center></td>
                    <td><center><a class="btn btn-xs btn-danger <?php echo (intval($gUser->online) == 0 ? 'disabled' : ''); ?>" onclick="showAmountDinc('<?php echo $gUser->id; ?>', '<?php echo $gUser->name; ?>')"  style="width: 100px">-</a></center></td>
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
<div class="modal fade" id="modelincriment" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="membername"></h4>
            </div>
            <form  action="<?php echo base_url() . 'amountincr'; ?>" method="POST">
                <div class="modal-body">
                    <center>
                        <div class="row">
                            <div class="col-lg-11">
                                <input type="text" class="input-lg form-control" id="valueiein" name="valueinput">
                            </div>
                            <div class="col-lg-1">
                                <h3>STC</h3>
                            </div>
                        </div>
                        <div class="row">
                            <?php
                            $Buttons = explode(',', $settings->chargebutton);
                            foreach ($Buttons as $btn):
                                ?>
                                <div class="col-lg-3">
                                    <a class="btn btn-default btn-block" onclick="setival('<?php echo $btn; ?>')"><?php echo $btn; ?></a>
                                </div>
                            <?php endforeach; ?>
                        </div>
                    </center>
                    <input type="hidden" name="groupid" id="groupid" value="<?php echo $GSelect; ?>">
                    <input type="hidden" name="userid" id="userid" value="">
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <div class="col-lg-10">
                            <button type="submit" class="btn btn-success btn-block">Зачислить</button>
                        </div>
                        <div class="col-lg-2">
                            <button type="button" class="btn btn-default" data-dismiss="modal">отменить</button>        
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="modeldeincriment" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="membernamed"></h4>
            </div>

            <form action="<?php echo base_url() . 'amountdinc'; ?>" method="POST">
                <div class="modal-body">
                    <center>
                        <div class="row">
                            <div class="col-lg-11">
                                <input type="text" class="input-lg form-control" id="valuedein" name="valueinput">
                            </div>
                            <div class="col-lg-1">
                                <h3>STC</h3>
                            </div>
                        </div>
                        <div class="row">
                            <?php
                            $Buttons = explode(',', $settings->chargebutton);
                            foreach ($Buttons as $btn):
                                ?>
                                <div class="col-lg-3">
                                    <a class="btn btn-default btn-block" onclick="setdval('<?php echo $btn; ?>')"><?php echo $btn; ?></a>
                                </div>
                            <?php endforeach; ?>
                        </div>
                    </center>
                    <input type="hidden" name="groupids" id="groupids" value="<?php echo $GSelect; ?>">
                    <input type="hidden" name="useridd" id="useridd" value="">
                </div>
                <div class="modal-footer">
                    <div class="row">
                        <div class="col-lg-10">
                            <button type="submit" class="btn btn-danger btn-block">Списать</button>
                        </div>
                        <div class="col-lg-2">
                            <button type="button" class="btn btn-default" data-dismiss="modal">отменить</button>        
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
