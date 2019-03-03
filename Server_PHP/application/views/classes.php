<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li class="active"><a href="<?php echo base_url() . 'classes'; ?>">Отчете по классам</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li><a href="<?php echo base_url() . 'report'; ?>">Отчете по студентам</a></li>
        <li><a href="<?php echo base_url() . 'teachers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'students'; ?>">Студенты</a></li>
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
                        <label for="tetchcomp">Преподаватель:</label>
                        <div class="autocomplete">
                            <input id="tetchcomp" class="form-control input-group" value="ВСЕ" type="text" name="tetchcomp" placeholder="ФИО">
                        </div>
                    </div>
                </div>
                <div class="col-sm-5">
                    <div class="form-group">
                        <label for="grorepo">Группы:</label>
                        <select class="form-control" id="grorepo">
                            <?php if (!is_null($Groplistgeneral)): ?>
                                <option value="-1" selected >ВСЕ</option>
                                <option value="" disabled="disabled">──────</option>
                                <?php foreach ($Groplistgeneral as $group): ?>
                                    <option value="<?php echo $group->id; ?>"><?php echo $group->groupname; ?></option>
                                <?php endforeach; ?>
                            <?php endif; ?>
                        </select>
                    </div>
                </div>
                <br>
                <div class="col-sm-2">
                    <button type="button" onclick="getClassReport()" id="getReportbtn" class="btn btn-block btn-lg btn-success">СФОРМИРОВАТЬ</button>
                </div>
            </div>
        </div>
        <hr>
        <table class="table table-bordered" id="dataTable" width="100%" cellspacing="0">
            <thead>
                <tr>
                    <th>№</th>
                    <th>Дата начала</th>
                    <th>Дата конца</th>
                    <th>Тема урока</th>
                    <th>Группа</th>
                    <th>Интересно</th>
                    <th>Сложно</th>
                    <th>Голосов</th>
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
                            <td><?php echo $rRepo->label; ?></td>
                            <td><?php echo $GroupName; ?></td>
                            <td><?php echo (is_null($rRepo->isithard) ? 0 : $rRepo->isithard); ?></td>
                            <td><?php echo (is_null($rRepo->por) ? 0 : $rRepo->por); ?></td>
                            <td><?php echo $rRepo->isithard + $rRepo->por; ?></td>
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
<script>
    let dataname = [];
<?php if (!is_null($tetcher)): ?>
        dataname.push("ВСЕ");
    <?php foreach ($tetcher as $tet): ?>
            dataname.push("<?php echo $tet->name; ?>");
    <?php endforeach; ?>
<?php endif; ?>
</script>
