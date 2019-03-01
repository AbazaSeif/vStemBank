<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">Отчете по классам</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li><a href="<?php echo base_url() . 'report'; ?>">Отчете по студентам</a></li>
        <li><a href="<?php echo base_url() . 'teachers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li class="active"><a href="<?php echo base_url() . 'students'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>

<div class="tab-content">
    <div id="home" class="tab-pane fade in active">
        <h3>Студенты</h3>
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
                <form id="formstudent" action="<?php echo base_url() . 'createstuding'; ?>" method="POST" enctype="multipart/form-data">      
                    <div class="col-sm-4">
                        <label for="name">ФИО</label>
                        <input type="text" required name="name" id="name" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="cardid">Карта</label>
                        <a href="" onclick="scancard()" data-toggle="modal" data-target="#scaning" class="btn btn-xs btn-danger">Scan</a>
                        <input type="text" readonly required name="cardid" id="cardid" class="form-control input-group">
                    </div>
                    <div class="col-sm-3">
                        <label for="name">ФИО родителя</label>
                        <input type="text" name="mothername" id="mothername" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="birthdate">Дата рождения</label>
                        <input type="date" name="birthdate" id="birthdate" class="form-control input-group">
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
                    <div class="col-sm-3">
                        <label for="phone">Телефон родителя</label>
                        <input type="tel" name="motherphone" id="motherphone" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="phone">Телефон</label>
                        <input type="tel" name="phone" id="phone" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="note">Комментарий</label>
                        <input type="text" name="note" id="note" class="form-control input-group">
                    </div>
            </div>
            <br>
            <hr>
            <input type="hidden" name="userid" id="userid">
            <input type="hidden" name="image" id="imagepath" value="">
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
        <table class="table table-bordered" id="dataTablestudent" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>№</th>
                    <th>ФИО</th>
                    <th>Телефон</th>
                    <th>Группа</th>
                    <th>Баланс</th>
                    <th>Множитель</th>
                    <th>Комментарий</th>
                    <th><center>Зачислить</center></th>
            <th><center>Списать</center></th>
            <th><center>действие</center></th>
            </tr>
            </thead>
            <tbody>
                <?php
                if (!is_null($lstudents)):
                    $index = 1;
                    ?>
                    <?php foreach ($lstudents as $gUser): ?>
                        <tr id="<?php echo $gUser->id; ?>">
                            <td><?php echo $index++; ?></td>
                            <td><?php echo ($gUser->isBlock ? '<strike>' . $gUser->name . '</strike>' : $gUser->name); ?> </td>
                            <td><?php echo $gUser->phonenumber; ?></td>
                            <td><?php echo $gUser->groups; ?></td>
                            <td><?php echo $gUser->amount; ?></td>
                            <td><?php echo $gUser->factor; ?></td>
                            <td><?php echo $gUser->note1; ?></td>
                            <td><center><a class="btn btn-xs btn-success" onclick="sshowAmountInc('<?php echo $gUser->id; ?>', '<?php echo $gUser->name; ?>')" style="width: 100px">+</a></center></td>
                    <td><center><a class="btn btn-xs btn-danger" onclick="sshowAmountDinc('<?php echo $gUser->id; ?>', '<?php echo $gUser->name; ?>')"  style="width: 100px">-</a></center></td>
                    <td><center>
                        <?php if ($gUser->isBlock): ?>
                            <a onclick="sunblock(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-warning">открыть</a>
                        <?php else: ?>
                            <a onclick="sblock(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-warning">Заблокирован</a>
                        <?php endif; ?>
                        <a onclick="sdelet(<?php echo $gUser->id; ?>)" class="btn btn-xs btn-danger">Удалить</a>
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
            <form action="<?php echo base_url() . 'studaddtogroup'; ?>" method="POST">
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
                    <input type='hidden' name="userlistid" id="userlistid" value="">
                    <button type="submit" class="btn btn-success">добавлять</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Закрыть</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="modal fade" id="modelincriment" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title" id="membername"></h4>
            </div>
            <form  action="<?php echo base_url() . 'studaddmoney'; ?>" method="POST">
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
                    <input type="hidden" name="useridmoney" id="useridmoney" value="">
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

            <form action="<?php echo base_url() . 'studgetmoney'; ?>" method="POST">
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
                    <input type="hidden" name="useridmoneyd" id="useridmoneyd" value="">
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

