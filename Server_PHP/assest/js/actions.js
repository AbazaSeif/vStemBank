let timer = new easytimer.Timer();
function selectgroup() {
    var groupid = $("#group").val();
    var URL = $("#murl").val();
    $("#actionbtn").attr("href", "");
    $("#actionbtn").attr("href", URL + "home/" + groupid);
    $("#actionbtn").show();
}

function showAmountInc(id, name) {
    $("#membername").html(name);
    $("#userid").val(id);
    $('#modelincriment').modal('show');
}
function showAmountDinc(id, name) {
    $("#membernamed").html(name);
    $("#useridd").val(id);
    $('#modeldeincriment').modal('show');
}
function sshowAmountInc(id, name) {
    $("#membername").html(name);
    $("#useridmoney").val(id);
    $('#modelincriment').modal('show');
}
function sshowAmountDinc(id, name) {
    $("#membernamed").html(name);
    $("#useridmoneyd").val(id);
    $('#modeldeincriment').modal('show');
}

function setival(value) {
    $("#valueiein").val(value);
}

function setdval(value) {
    $("#valuedein").val(value);
}

function startlesson() {
    var URL = $("#murl").val();
    var status = $("#btnstarttot").html();
    if (status == 'Начните Урок') {
        $("#btnstarttot").removeClass('btn-success');
        $("#btnstarttot").addClass('btn-danger');
        $("#btnstarttot").html('заверщить урок');
        var lblless = $("#labellesson").val();
        var groless = $("#groupidlesson").val();
        $.post(URL + 'startlessonf', {
            lbl: lblless,
            gro: groless
        }, function (data) {
            $("#amoutn").html($.trim(data));
            timer.start();
            timer.addEventListener('secondsUpdated', function (e) {
                $('#timerler').html(timer.getTimeValues().toString());
            });
        });
    } else {
        $("#btnstarttot").removeClass('btn-danger');
        $("#btnstarttot").addClass('btn-success');
        $("#btnstarttot").html('Начните Урок');
        var groless = $("#groupidlesson").val();
        $.post(URL + 'endlessonf', {
            gro: groless
        }, function (data) {
            $("#amoutn").html($.trim(data));
            timer = null;
            $('#timerler').html("00:00:00");
            location.reload();
        });
    }
}

function getReport() {
    var URL = $("#murl").val();
    var Tetcher = $("#tetchcomp").val();
    var Group = $("#grorepo").val();
    $.post(URL + 'actionreport', {
        groupid: Group,
        tetcherid: Tetcher
    }, function () {
        location.reload();
    });
}

function getClassReport() {
    var URL = $("#murl").val();
    var Tetcher = $("#tetchcomp").val();
    var Group = $("#grorepo").val();
    $.post(URL + 'actionclasses', {
        groupid: Group,
        tetcherid: Tetcher
    }, function () {
        location.reload();
    });
}

function readURL(input) {
    var URL = $("#murl").val();
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
            $('#blah').attr('src', e.target.result);
            $.post(URL + 'imageupload', {
                img: e.target.result
            }, function (data) {
                $("#imagepath").val($.trim(data));
            });
        }
        reader.readAsDataURL(input.files[0]);
    }
}

$("#imgInp").change(function () {
    $('#imagepath').files = $(this).files;
    readURL(this);
});
function scancard() {
    var URL = $("#murl").val();
    $.post(URL + 'scan', null, function (data) {
        $("#cardid").val($.trim(data));
        $("#scaning").modal('hide');
    });
}

function admin(id) {
    var URL = $("#murl").val();
    $.post(URL + 'asadmin', {
        uid: id
    }, function () {
        location.reload();
    });
}
function unadmin(id) {
    var URL = $("#murl").val();
    $.post(URL + 'asuser', {
        uid: id
    }, function () {
        location.reload();
    });
}
function block(id) {
    var URL = $("#murl").val();
    $.post(URL + 'asblock', {
        uid: id
    }, function () {
        location.reload();
    });
}
function unblock(id) {
    var URL = $("#murl").val();
    $.post(URL + 'asunblock', {
        uid: id
    }, function () {
        location.reload();
    });
}
function delet(id) {
    var URL = $("#murl").val();
    $.post(URL + 'asdelete', {
        uid: id
    }, function () {
        location.reload();
    });
}
function gdelet(id) {
    var URL = $("#murl").val();
    $.post(URL + 'delgroup', {
        uid: id
    }, function () {
        location.reload();
    });
}

function gisdone(id) {
    var URL = $("#murl").val();
    $.post(URL + 'closegroup', {
        uid: id
    }, function () {
        location.reload();
    });
}
function gnotdone(id) {
    var URL = $("#murl").val();
    $.post(URL + 'opengroup', {
        uid: id
    }, function () {
        location.reload();
    });
}
function addtogroup(id) {
    $("#userid").val(id);
    $('#addingroup').modal('show');
}

function sblock(id) {
    var URL = $("#murl").val();
    $.post(URL + 'studblock', {
        uid: id
    }, function () {
        location.reload();
    });
}
function sunblock(id) {
    var URL = $("#murl").val();
    $.post(URL + 'studunblock', {
        uid: id
    }, function () {
        location.reload();
    });
}
function sdelet(id) {
    var URL = $("#murl").val();
    $.post(URL + 'studdelete', {
        uid: id
    }, function () {
        location.reload();
    });
}
function saddtogroup(id) {
    $("#userlistid").val(id);
    $('#addingroup').modal('show');
}

function poweroff(id) {
    var URL = $("#murl").val();
    $.post(URL + 'poweroff', {
        pc: id
    }, function () {
        location.reload();
    });
}
