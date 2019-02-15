<?php if ($this->session->flashdata('success')): ?>
    <div class="alert alert-success">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <?php echo $this->session->flashdata('success'); ?>.
    </div>
    <?php $this->session->unmark_flash('success'); ?>
<?php endif; ?>

<?php if ($this->session->flashdata('error')): ?>
    <div class="alert alert-danger">
        <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
        <strong>Ошибка !</strong> <?php echo $this->session->flashdata('error'); ?>.
    </div>
    <?php $this->session->unmark_flash('error'); ?>
<?php endif; ?>
