<div class="row">
    <?php if (!is_null($computers)): ?>
        <?php foreach ($computers as $pc): ?>
            <div class="col-lg-2">
                <center>
                    <img src="assest/computer.png" width="100" height="100"><br>
                    <label class="label label-success"><?php echo $pc->name; ?></label><br>
                    <button onclick="poweroff(<?php echo $pc->id; ?>)" class="btn btn-xs btn-danger btn-block">Power Off</button>
                </center>
            </div>
        <?php endforeach; ?>
    <?php endif; ?>
</div>
