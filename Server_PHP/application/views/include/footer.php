<div class="modal fade" id="admincode" role="dialog">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header bg-danger">
                <button type="button" class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">Панель администратора</h4>
            </div>
            <form action="<?php echo base_url() . 'admincode'; ?>" method="POST">
                <div class="modal-body">
                    <label for="code">Код</label>    
                    <input type="password" class="form-control input-sm" name="code">
                    <label for="pass">пароль</label>    
                    <input type="password" class="form-control input-sm" name="pass">
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-default">Войти</button>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="<?php echo base_url(); ?>assest/js/bootstrap.js"></script>
<script src="<?php echo base_url(); ?>assest/js/easytimer.min.js"></script>
<script src="<?php echo base_url(); ?>assest/js/datatables/dataTables.bootstrap4.js"></script>
<script src="<?php echo base_url(); ?>assest/js/datatables/jquery.dataTables.js"></script>
<script src="<?php echo base_url(); ?>assest/js/actions.js"></script>
</html>
