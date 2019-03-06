$(document).ready(function () {
    var DataTable = {
        "language": {
            "decimal": "",
            "emptyTable": "В таблице нет значений",
            "info": "Отображено с _START_ по _END_ of _TOTAL_ строк",
            "infoEmpty": "Отображено с 0 по 0 из 0 строк",
            "infoFiltered": "(filtered from _MAX_ total entries)",
            "infoPostFix": "",
            "thousands": ",",
            "lengthMenu": "Отобразить _MENU_ строк",
            "loadingRecords": "Загрузка...",
            "processing": "Обработка...",
            "search": "Поиск:",
            "zeroRecords": "Не найдено подходящих значений",
            "paginate": {
                "first": "Первая",
                "last": "Последняя",
                "next": "Следующая",
                "previous": "Предыдущая"
            },
            "aria": {
                "sortAscending": ": Сортировать по возрастанию",
                "sortDescending": ": Сортировать по убыванию"
            }
        },
        aLengthMenu: [[25, 50, 75, -1], [25, 50, 75, "ВСЕ"]],
        iDisplayLength: 50,
        rowReorder: true,
        fixedColumns: true,
    };

    $('#dataTable').DataTable(DataTable);
    $('#dataTabletetchers').DataTable(DataTable);
    $('#dataTablegroups').DataTable(DataTable);
    $('#dataTablestudent').DataTable(DataTable);
    $("#dataTablelesson").DataTable(DataTable);

    var dataTabletetchers = $('#dataTabletetchers').DataTable();
    var dataTablegroups = $('#dataTablegroups').DataTable();
    var dataTablestudent = $('#dataTablestudent').DataTable();

    let URL = $("#murl").val();
    $('#dataTabletetchers tbody').on('click', 'tr', function () {
        if ($(this).hasClass('bg-primary')) {
            $(this).removeClass('bg-primary');
            $("#nametet").val("");
            $("#birthdaytet").val("");
            $("#phonetet").val("");
            $("#notetet").val("");
            $("#passtet").val("");
            $("#uid").val("");
            $("#blah").attr('src', URL + "assest/user.png");
            $("#grops option:selected").prop("selected", false)
            $("#tetcherform").attr("action", URL + "datasave");
            $("#actionbtn").html("Создать");
            $("#cancelbtn").hide();
        } else {
            dataTabletetchers.$('tr.bg-primary').removeClass('bg-primary');
            $(this).addClass('bg-primary');
            var tin = $(this).attr('id');
            $.post(URL + 'tinfo', {tetinfo: tin}, function (data) {
                if (data !== '') {
                    var rData = JSON.parse(data);
                    $("#nametet").val(rData.name);
                    $("#birthdaytet").val(rData.birthdate);
                    $("#phonetet").val(rData.phonenumber);
                    $("#notetet").val(rData.note1);
                    $("#passtet").val(rData.password);
                    $("#userid").val(rData.id);
                    if (rData.image !== "") {
                        $("#blah").attr('src', URL + "image_upload/" + rData.image);
                    }
                    var Group = JSON.parse(rData.group);
                    $("#grops option:selected").prop("selected", false)
                    $.each(Group, function (key, value) {
                        $("#grops option[value=" + value.id + "]").prop('selected', true);
                    });
                    $("#tetcherform").attr("action", URL + "dataupdate");
                    $("#actionbtn").html("обновление");
                    $("#cancelbtn").show();
                }
            });
        }
    });

    $('#cancelbtn').click(function () {
        dataTabletetchers.$('tr.bg-primary').removeClass('bg-primary');
        $("#nametet").val("");
        $("#birthdaytet").val("");
        $("#phonetet").val("");
        $("#notetet").val("");
        $("#passtet").val("");
        $("#uid").val("");
        $("#grops option:selected").prop("selected", false)
        $("#blah").attr('src', URL + "assest/user.png");
        $("#tetcherform").attr("action", URL + "datasave");
        $("#actionbtn").html("Создать");
        $("#cancelbtn").hide();
    });

    //Groups
    $('#dataTablegroups tbody').on('click', 'tr', function () {
        if ($(this).hasClass('bg-primary')) {
            $(this).removeClass('bg-primary');
            $("#gname").val("");
            $("#gitemname").val("");
            $("#desc").val("");
            $("#gid").val("");
            $("#cancelbtn").hide();
            $("#actionbtn").html("Создать");
            $("#groupform").attr('action', URL + 'creategroup');

        } else {
            dataTablegroups.$('tr.bg-primary').removeClass('bg-primary');
            $(this).addClass('bg-primary');
            var tin = $(this).attr('id');
            $.post(URL + 'ginfo', {groinfo: tin}, function (data) {
                if (data !== '') {
                    var rData = JSON.parse(data);
                    $("#gname").val(rData.groupname);
                    $("#gitemname").val(rData.materials);
                    $("#desc").val(rData.description);
                    $("#gid").val(rData.id);
                    $("#actionbtn").html("обновление");
                    $("#cancelbtn").show();
                    $("#groupform").attr('action', URL + 'upgradegroup');
                }
            });
        }
    });

    $('#cancelbtn').click(function () {
        dataTablegroups.$('tr.bg-primary').removeClass('bg-primary');
        $("#gname").val("");
        $("#gitemname").val("");
        $("#desc").val("");
        $("#cancelbtn").hide();
        $("#actionbtn").html("Создать");
        $("#groupform").attr('action', URL + 'creategroup');
    });


    //Student
    $('#dataTablestudent tbody').on('click', 'tr', function () {
        if ($(this).hasClass('bg-primary')) {
            $(this).removeClass('bg-primary');
            $("#name").val("");
            $("#cardid").val("");
            $("#mothername").val("");
            $("#birthdate").val("");
            $("#motherphone").val("");
            $("#phone").val("");
            $("#note").val("");
            $("#userid").val("");
            $("#blah").attr('src', URL + "assest/user.png");
            $("#grops option:selected").prop("selected", false)
            $("#formstudent").attr("action", URL + "createstuding");
            $("#actionbtn").html("Создать");
            $("#cancelbtn").hide();

        } else {
            dataTablestudent.$('tr.bg-primary').removeClass('bg-primary');
            $(this).addClass('bg-primary');
            var tin = $(this).attr('id');
            $.post(URL + 'stinfo', {stinfo: tin}, function (data) {
                if (data !== '') {
                    var rData = JSON.parse(data);
                    $("#name").val(rData.name);
                    $("#cardid").val(rData.cardid);
                    $("#mothername").val(rData.parentname);
                    $("#birthdate").val(rData.birthdate);
                    $("#motherphone").val(rData.parentphone);
                    $("#phone").val(rData.phonenumber);
                    $("#note").val(rData.notes1);
                    $("#userid").val(rData.id);
                    if (rData.image !== "") {
                        $("#blah").attr('src', URL + "image_upload/" + rData.image);
                    } else {
                        $("#blah").attr('src', URL + "assest/user.png");
                    }
                    var Group = JSON.parse(rData.group);
                    $("#grops option:selected").prop("selected", false)
                    $.each(Group, function (key, value) {
                        $("#grops option[value=" + value.id + "]").prop('selected', true);
                    });
                    $("#formstudent").attr("action", URL + "studupdate");
                    $("#actionbtn").html("обновление");
                    $("#cancelbtns").show();
                }
            });
        }
    });

    $('#cancelbtns').click(function () {
        dataTablestudent.$('tr.bg-primary').removeClass('bg-primary');
        $("#name").val("");
        $("#cardid").val("");
        $("#mothername").val("");
        $("#birthdate").val("");
        $("#motherphone").val("");
        $("#phone").val("");
        $("#note").val("");
        $("#userid").val("");
        $("#blah").attr('src', URL + "assest/user.png");
        $("#grops option:selected").prop("selected", false)
        $("#formstudent").attr("action", URL + "createstuding");
        $("#actionbtn").html("Создать");
        $("#cancelbtns").hide();
    });


});
function selectgroup() {
    let groupid = $("#group").val();
    let URL = $("#murl").val();
    location.replace(URL + "home/" + groupid);
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
function checktime() {
    var URL = $("#murl").val();
    $.post(URL + 'checktime', null, function (data) {
        try {
            var d = JSON.parse(data);
            $('#timerler').html(d.time);
            $('#amoutn').html(d.amount);
        } catch (e) {
        }
    });
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
        }, function () {
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
        studname: Tetcher
    }, function () {
        location.reload(URL + 'report');
    });
}

function getClassReport() {
    var URL = $("#murl").val();
    var Tetcher = $("#tetchcomp").val();
    var Group = $("#grorepo").val();
    $.post(URL + 'actionclasses', {
        groupid: Group,
        tetchername: Tetcher
    }, function () {
        location.reload(URL + 'classes');
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
function edit(id) {
    var URL = $("#murl").val();
    location.replace(URL + 'edit/' + id);
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
function sopendir(id) {
    var URL = $("#murl").val();
    $.post(URL + 'opendirectory', {
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
function closeallcomputer() {
    var URL = $("#murl").val();
    $.post(URL + 'poweroffall', null, function () {
        location.reload();
    });
}


function autocomplete(inp, arr) {
    /*the autocomplete function takes two arguments,
     the text field element and an array of possible autocompleted values:*/
    var currentFocus;
    /*execute a function when someone writes in the text field:*/
    inp.addEventListener("input", function (e) {
        var a, b, i, val = this.value;
        /*close any already open lists of autocompleted values*/
        closeAllLists();
        if (!val) {
            return false;
        }
        currentFocus = -1;
        /*create a DIV element that will contain the items (values):*/
        a = document.createElement("DIV");
        a.setAttribute("id", this.id + "autocomplete-list");
        a.setAttribute("class", "autocomplete-items");
        /*append the DIV element as a child of the autocomplete container:*/
        this.parentNode.appendChild(a);
        /*for each item in the array...*/
        for (i = 0; i < arr.length; i++) {
            /*check if the item starts with the same letters as the text field value:*/
            if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                /*create a DIV element for each matching element:*/
                b = document.createElement("DIV");
                /*make the matching letters bold:*/
                b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                b.innerHTML += arr[i].substr(val.length);
                /*insert a input field that will hold the current array item's value:*/
                b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                /*execute a function when someone clicks on the item value (DIV element):*/
                b.addEventListener("click", function (e) {
                    /*insert the value for the autocomplete text field:*/
                    inp.value = this.getElementsByTagName("input")[0].value;
                    /*close the list of autocompleted values,
                     (or any other open lists of autocompleted values:*/
                    closeAllLists();
                });
                a.appendChild(b);
            }
        }
    });
    /*execute a function presses a key on the keyboard:*/
    inp.addEventListener("keydown", function (e) {
        var x = document.getElementById(this.id + "autocomplete-list");
        if (x)
            x = x.getElementsByTagName("div");
        if (e.keyCode == 40) {
            /*If the arrow DOWN key is pressed,
             increase the currentFocus variable:*/
            currentFocus++;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 38) { //up
            /*If the arrow UP key is pressed,
             decrease the currentFocus variable:*/
            currentFocus--;
            /*and and make the current item more visible:*/
            addActive(x);
        } else if (e.keyCode == 13) {
            /*If the ENTER key is pressed, prevent the form from being submitted,*/
            e.preventDefault();
            if (currentFocus > -1) {
                /*and simulate a click on the "active" item:*/
                if (x)
                    x[currentFocus].click();
            }
        }
    });
    function addActive(x) {
        /*a function to classify an item as "active":*/
        if (!x)
            return false;
        /*start by removing the "active" class on all items:*/
        removeActive(x);
        if (currentFocus >= x.length)
            currentFocus = 0;
        if (currentFocus < 0)
            currentFocus = (x.length - 1);
        /*add class "autocomplete-active":*/
        x[currentFocus].classList.add("autocomplete-active");
    }
    function removeActive(x) {
        /*a function to remove the "active" class from all autocomplete items:*/
        for (var i = 0; i < x.length; i++) {
            x[i].classList.remove("autocomplete-active");
        }
    }
    function closeAllLists(elmnt) {
        /*close all autocomplete lists in the document,
         except the one passed as an argument:*/
        var x = document.getElementsByClassName("autocomplete-items");
        for (var i = 0; i < x.length; i++) {
            if (elmnt != x[i] && elmnt != inp) {
                x[i].parentNode.removeChild(x[i]);
            }
        }
    }
    /*execute a function when someone clicks in the document:*/
    document.addEventListener("click", function (e) {
        closeAllLists(e.target);
    });
}

function compliteadduser() {
    var URL = $("#murl").val();
    $("#resetactionstudent").html("Подождите пожалуйста");
    $.post(URL + 'recreatestudent', null, function () {
        location.replace(URL + 'students');
    });
}
function canceladduser() {
    var URL = $("#murl").val();
    location.replace(URL + 'students');
}
setInterval(function () {
    checktime() // this will run after every 5 seconds
}, 1000);
try {
    $("#tetchcomp").keyup(function () {
        if ($(this).val() == "") {
            $("#getReportbtn").addClass('disabled');
        } else {
            $("#getReportbtn").removeClass('disabled');
        }
    });
    autocomplete(document.getElementById("tetchcomp"), dataname);
} catch (e) {
}
