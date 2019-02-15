<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li class="active"><a href="<?php echo base_url() . 'report'; ?>">Отчеты</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">класс</a></li>
    <?php if ($this->session->admin): ?>
        <li><a href="<?php echo base_url() . 'tetchers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'lstudints'; ?>">Студенты</a></li>
        <li><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>
<div class="tab-content">
    <div class="tab-pane fade in active">
        <h3>Отчеты</h3>
        <div class="well">
            <div class="row">
                <div class="col-sm-5">
                    <div class="form-group">
                        <label for="tetchcomp">ФИО ученика:</label>
                        <select class="form-control" id="tetchcomp">
                            <?php if (!is_null($tetcher)): ?>
                                <?php foreach ($tetcher as $tet): ?>
                                    <option value="<?php echo $tet->id; ?>" <?php echo ($this->session->id == $tet->id ? 'selected' : ''); ?>><?php echo $tet->name; ?></option>
                                <?php endforeach; ?>
                            <?php endif; ?>

                        </select>
                    </div>
                </div>
                <div class="col-sm-5">
                    <div class="form-group">
                        <label for="grorepo">Группы:</label>
                        <select class="form-control" id="grorepo">
                            <?php if (!is_null($Groplistgeneral)): ?>
                                <?php foreach ($Groplistgeneral as $group): ?>
                                    <option value="<?php echo $group->id; ?>"><?php echo $group->groupname; ?></option>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </select>
                    </div>
                </div>
                <br>
                <div class="col-sm-2">
                    <button type="button" onclick="getReport()" class="btn btn-block btn-lg btn-success">СФОРМИРОВАТЬ</button>
                </div>
            </div>
        </div>
        <hr>
        <table class="table table-condensed">
            <thead>
                <tr>
                    <th>№</th>
                    <th>Дата начала</th>
                    <th>Дата конца</th>
                    <th>Был?</th>
                    <th>Тема урока</th>
                    <th>Группа</th>
                    <th>Баланс</th>
                </tr>
            </thead>
            <tbody>
                <?php
                if (!is_null($dReport)):
                    $Index = 1;
                    ?>
                    <?php foreach ($dReport as $rRepo): ?>
                        <?php
                        foreach ($Groplistgeneral as $rs) {
                            if ($rs->id == $rRepo->groupid) {
                                $GroupName = $rs->groupname;
                            }
                        }
                        ?>
                        <tr class="<?php echo ($rRepo->status == 0 ? 'bg-success' : 'bg-danger'); ?>">
                            <td><?php echo $Index; ?></td>
                            <td><?php echo $rRepo->timestart; ?></td>
                            <td><?php echo $rRepo->timeend; ?></td>
                            <td><?php echo ($rRepo->status == 0 ? 'Да' : 'Нет'); ?></td>
                            <td><?php echo $rRepo->label; ?></td>
                            <td><?php echo $GroupName; ?></td>
                            <td><?php echo $rRepo->balance; ?></td>
                        </tr>
                        <?php
                        $Index++;
                    endforeach;
                    ?>
                <?php endif; ?>
            </tbody>
        </table>
    </div>
</div>
