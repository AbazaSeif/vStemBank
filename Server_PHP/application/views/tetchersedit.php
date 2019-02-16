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
                            <img src="<?php echo base_url() . (!empty($uData->image) ? 'image_upload/' . $uData->image : 'assest/user.png'); ?>" id="blah" width="120" height="140">
                            <label class="btn btn-default btn-block btn-file">
                                Browse <input id="imgInp" name="userfile" type="file" style="display: none;">
                            </label>
                        </form>
                    </center>
                </div>
                <form action="<?php echo base_url() . 'dataupdate'; ?>" method="POST" enctype="multipart/form-data">      
                    <div class="col-sm-4">
                        <label for="name">ФИО</label>
                        <input type="text" required name="name" value="<?php echo $uData->name; ?>" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="birthdate">Дата рождения</label>
                        <input type="date" required name="birthdate" value="<?php echo $uData->birthdate; ?>" class="form-control input-group">
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
                        <input required type="tel" value="<?php echo $uData->phonenumber; ?>" name="phone" class="form-control input-group">
                    </div>
                    <div class="col-sm-4">
                        <label for="note">Комментарий</label>
                        <input type="text" name="note" value="<?php echo $uData->notes1; ?>" class="form-control input-group">
                    </div>
                    <div style="padding-left: 9%;" class="col-sm-9">
                        <label for="note">пароль</label>
                        <input required type="text" name="password" value="<?php echo $uData->password; ?>" class="form-control input-group">
                    </div>
            </div>
            <br>
            <hr>
            <div style="padding-left: 10%;padding-right: 10%;">
                <input type="hidden" name="userid" id="userid" value="<?php echo $uData->id; ?>">
                <input type="hidden" name="image" id="imagepath" value="">
                <button type="submit" class="btn btn-success btn-block">обновление</button>
                <a href="<?php echo base_url() . 'tetchers'; ?>" class="btn btn-danger btn-block">отменить</a>
            </div>
            </form>
        </div>
    </div>
</div>
