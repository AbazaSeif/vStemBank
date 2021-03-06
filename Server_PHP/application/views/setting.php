<ul class="nav nav-tabs">
    <li><a href="<?php echo base_url() . 'home'; ?>">Урок</a></li>
    <li><a href="<?php echo base_url() . 'classes'; ?>">Отчеты по классам</a></li>
    <?php if ($this->session->isAdmin): ?>
        <li><a href="<?php echo base_url() . 'report'; ?>">Отчеты по студентам</a></li>
        <li><a href="<?php echo base_url() . 'teachers'; ?>">Преподаватели</a></li>
        <li><a href="<?php echo base_url() . 'ngroups'; ?>">Группы</a></li>
        <li><a href="<?php echo base_url() . 'students'; ?>">Студенты</a></li>
        <li class="active"><a href="<?php echo base_url() . 'setting'; ?>">Настройки</a></li>
    <?php endif; ?>
</ul>
<div class="tab-content">
    <div class="tab-pane fade in active">
        <h3>Настройки</h3>
        <form action="<?php echo base_url() . 'updatesetting'; ?>" method="POST">
            <div class="row">
                <div class="col-lg-3">
                    <label for="name">Заголовок сайта</label>
                    <input type="text" value="<?php echo $settings->name; ?>" name="name" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="startsession">Количество монет начисляемых преподавателю за урок (STC)</label>
                    <input type="number" value="<?php echo $settings->startsession; ?>" name="startsession" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="charge">Быстрые кнопки при начислении и списании</label><br>
                    <?php $ListB = explode(",", $settings->chargebutton); ?>
                    <input type="number" style="width: 15%;" value="<?php echo $ListB[0]; ?>" name="chargebutton[]" >
                    <input type="number" style="width: 15%;" value="<?php echo $ListB[1]; ?>" name="chargebutton[]" >
                    <input type="number" style="width: 15%;" value="<?php echo $ListB[2]; ?>" name="chargebutton[]" >
                    <input type="number" style="width: 15%;" value="<?php echo $ListB[3]; ?>" name="chargebutton[]" >
                </div>
                <div class="col-lg-3">
                    <label for="longcharge">Через какое время после начала урока начислить монеты преподавателю (мин)</label>
                    <input type="number" value="<?php echo $settings->longcharge; ?>" name="longcharge" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="sessiondelayed">Через какое время после начала урока считается опаздание (мин)</label>
                    <input type="number" value="<?php echo $settings->sessiondelayed; ?>" name="sessiondelayed" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="conductsurvey">Через какое время после начала урока провести опрос? (мин) </label>
                    <input type="number" value="<?php echo $settings->conductsurvey; ?>" name="conductsurvey" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="votingcoin">Сколько монет начислить за голосование?</label>
                    <input type="number" value="<?php echo $settings->votingcoin; ?>" name="votingcoin" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="">Сколько монет начислить за посещение без опаздания?</label>
                    <input type="number" value="<?php echo $settings->rewardamount; ?>" name="rewardamount" class="form-control input-group">
                </div>
                <div class="col-lg-3">
                    <label for="">Максимальное время урока (час)</label>
                    <input type="number" value="<?php echo $settings->endlesson; ?>" name="endlesson" class="form-control input-group">
                </div>
            </div>
            <br><hr>
            <div class="row">
                <div class="col-lg-12">
                    <button type="submit" class="btn btn-success btn-block btn-lg">Обновить</button>
                </div>
            </div>
        </form>
    </div>
</div>
