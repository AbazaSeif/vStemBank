<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">Отчете по классам</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li class="active"><a href="<?php echo base_url() . 'report'; ?>">Отчете по студентам</a></li>
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
                        <label for="tetchcomp">ФИО ученика:</label>
                        <div class="autocomplete">
                            <input id="tetchcomp" class="form-control input-group" type="text" name="tetchcomp" placeholder="ФИО">
                        </div>
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
                                <option value="" disabled="disabled">──────</option>
                                <option value="-1" >ВСЕ</option>
                            <?php endif; ?>
                        </select>
                    </div>
                </div>
                <br>
                <div class="col-sm-2">
                    <button type="button" onclick="getReport()" id="getReportbtn" class="btn btn-block btn-lg btn-success disabled">СФОРМИРОВАТЬ</button>
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
                            if ($rs->id == $rRepo['groupid']) {
                                $GroupName = $rs->groupname;
                            }
                        }
                        ?>
                        <tr class="<?php echo ($rRepo['Exist'] == true ? 'bg-success' : 'bg-danger'); ?>">
                            <td><?php echo $Index; ?></td>
                            <td><?php echo $rRepo['LoginTime']; ?></td>
                            <td><?php echo $rRepo['TimeEnd']; ?></td>
                            <td><?php echo ($rRepo['Exist'] == true ? 'Да' : 'Нет'); ?></td>
                            <td><?php echo $rRepo['Label']; ?></td>
                            <td><?php echo $GroupName; ?></td>
                            <td><?php echo $rRepo['Balance']; ?></td>
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
        dataname.push("BCE");
    <?php foreach ($tetcher as $tet): ?>
            dataname.push("<?php echo $tet->name; ?>");
    <?php endforeach; ?>
<?php endif; ?>
</script>
