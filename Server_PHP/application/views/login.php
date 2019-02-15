
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
    <div class="container">
        <form accept-charset="UTF-8" action="<?php echo $basepath . 'login'; ?>" class="row" autocomplete="off" method="POST">
            <div class="col-sm-3">
                <label for="name">Логин</label>
            </div>
            <div class="col-sm-9">
                <input name="username" type="text" class="form-control" />
            </div>
            <br><br>
            <div class="col-sm-3">
                <label for="pass">пароль</label>
            </div>
            <div class="col-sm-9">
                <input name="password" type="password" class="form-control" />
            </div>
            <br><br><br>
            <div class="col-sm-12">
                <button type="submit" class="btn btn-block btn-success" value="Submit">войти</button>
            </div>
            <br><br><br><br><br>
        </form>
    </div>
</div>

