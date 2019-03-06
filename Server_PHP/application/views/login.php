<?php if ($this->session->flashdata('error')): ?>
    <div class="alert alert-danger">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <strong>Ошибка !</strong> <?php echo $this->session->flashdata('error'); ?>.
    </div>
    <?php $this->session->unmark_flash('error'); ?>
<?php endif; ?>

<div id="container">
    <center>
        <h1><?php echo $welcome; ?></h1>
    </center>
    <div class="container" style="padding-left: 10%;padding-right: 10%;">
        <form accept-charset="UTF-8" action="<?php echo $basepath . 'login'; ?>" class="row" autocomplete="off" method="POST">
            <div class="col-sm-9">
                <div class="form-group">
                    <label for="login">Логин</label>
                    <select name="username" class="form-control" id="login">
                        <?php rsort($UList); ?>
                        <?php foreach ($UList as $users): ?>
                            <option value="<?php echo $users->cardid; ?>"><?php echo $users->name; ?></option>
                        <?php endforeach; ?>
                    </select>
                </div>
            </div>
            <br><br>
            <div class="col-sm-9">
                <label for="password">Пароль</label>
                <input name="password" type="password" placeholder="пароль" class="form-control" />
            </div>
            <div class="col-sm-9">
                <br>
                <button type="submit" class="btn btn-block btn-success" value="Submit">Войти</button>
                <br>
            </div>
        </form>
    </div>
</div>

