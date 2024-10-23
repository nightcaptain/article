
var $$ = mdui.JQ;
$(document).ready(function () {
  $$('#btn_next_p').hide();
  $$('#fab_btn').hide();
});


function add_condition() {
    var content = '<div id="additional_condition" class="mdui-row mdui-m-t-1 mdui-valign" >\n' +
        '\n' +
        '            <div id="search_condition" class="mdui-col-xs-1 mdui-col-offset-xs-1  mdui-valign" style="margin-top: 5px;">\n' +
        '\n' +
        '                 <select id="logic-condition" class="mdui-select" mdui-select>\n' +
        '                  <option value="1">并且</option>\n' +
        '                  <option value="2">或者</option>\n' +
        '                  <option value="3">非</option>\n' +
        '                </select>\n' +
        '\n' +
        '             </div>\n' +
        '\n' +
        '             <div id="" class="mdui-col-xs-1 mdui-valign" style="margin-top: 5px;">\n' +
        '\n' +
        '                 <select id="select-condition" class="mdui-select" mdui-select>\n' +
        '                     <option value="2">题名</option>\n' +
        '                     <option value="3">关键词</option>\n' +
        '                     <option value="4">摘要</option>\n' +
        '                     <option value="6">作者</option>\n' +
        '                     <option value="7">来源</option>\n' +
        '                </select>\n' +
        '\n' +
        '             </div>\n' +
        '\n' +
        '            <div id="" class="mdui-col-xs-8 mdui-valign mdui-textfield">\n' +
        '\n' +
        '              <input id="keyword" name="keyword" class="mdui-textfield-input" type="text" value=""/>\n' +
        '\n' +
        '            </div>\n' +
        '        </div>';

    var add_conditions = $('#add_conditions');
    add_conditions.append(content);

}

function remove_condition() {
    $('#additional_condition:last-child').remove();
}

function reload_formula() {
    var base_condition = $('#base_condition');
    var base_expression = get_formula1(base_condition.find('#select-condition'), base_condition.find('#keyword'));
    if (base_expression.length == 0){
        return "";
    }

    return encodeURI(base_expression)
}

function get_formula1(selectele, inputele) {
    console.log($('#select-condition').val());
    var selectval = selectele.val();
    var inputval = inputele.val();
    var field, sign, targetexpression;

    if (inputval == null || inputval == undefined || inputval.length == 0) {
        return "";
    }

    switch (selectval) {
        case '1': field = "SU"; break;
        case '2': field = "TI"; break;
        case '3': field = "KY"; break;
        case '4': field = "AB"; break;
        case '5': field = "FT"; break;
        case '6': field = "AU"; break;
        case '7': field = "LY"; break;
        case '8': field = "JN"; break;
        default: field = "TI";
    }
    sign = 'like'
    targetexpression = field+" "+sign+" "+"'%"+inputval+"%'";
    // console.log(targetexpression);
    return targetexpression;
}

function get_formula2(logicele, selectele, inputele) {
    var base_exp = get_formula1(selectele, inputele);
    var logicval = logicele.val();
    var field, target_exp;

    if (base_exp == null || base_exp == undefined || base_exp.length == 0){
        return "";
    }

    switch (logicval) {
        case '1': field = "AND"; break;
        case '2': field = "OR"; break;
        case '3': field = "NOT"; break;
        default: field = "AND";
    }

    target_exp = " " + field + " " + base_exp;
    // console.log(target_exp);
    return target_exp;
}

function do_search(start) {
    var db = get_db();
    var exp = reload_formula();
    var types =$('input[name="group1"]:checked').val()
    var time_start = $('#time_start option:selected').val()
    var time_end = $('#time_end option:selected').val()
    if (exp.length == 0){
        mdui.alert('检索内容为空');
        return;
    }
    var ldialog = new mdui.Dialog('#loading_dialog', {modal: false});
    ldialog.open();
    if (start == null || start == undefined) {
        start = '1';
    }

    var url = "/findArticle?keywords="+exp+"&type="+types+"&startDate="+time_start+"&endDate="+time_end+"&page="+start
    $.getJSON(url, function (res) {
        console.log(res);
        $("#result_main").children().remove()
        if(res.item.length>0){
            var str =   "<div class='mdui-col-xs-12 mdui-valign mdui-text-color-black-secondary'><table border='1' cellpadding='20px'" +
                " cellspacing='0' style='text-align: center;margin-left: 78px'>" +
                "<tr>" +
                "<td>标题</td>" +
                "<td>作者</td>" +
                "<td>来源</td>" +
                "<td>关键字</td>" +
                "<td>下载次数</td>" +
                "<td>转发次数</td></tr>";
            $.each(res.item,function (i,item) {
                str+="<tr>"+
                    "<td><a href='/search?keyword="+item.ti+"&type="+types+"' target='_blank'>"+item.ti+"</a></td>" +
                    "<td>"+item.au+"</td>" +
                    "<td>"+item.ly+"</td>" +
                    "<td>"+item.ky+"</td>" +
                    "<td>"+item.downloadTimes+"</td>" +
                    "<td>"+item.referTimes+"</td>" + "</tr>"
            })

            str+="</tr></div>"

            $("#result_main").html(str)
        }else{
            var str="<div class='mdui-col-xs-12 mdui-valign mdui-text-color-black-secondary'>"+
                        "<span class='mdui-center'><h3>暂无数据</h3> </span></div>"
            $("#result_main").html(str)
        }
        ldialog.close();
        $$('#btn_next_p').show();
        $$('#btn_next_p #btn_next').attr("onclick","do_search("+(res.page+1)+")")
        $$('#fab_btn').show();
        //set_nextcontent(exp, res.page+1);
    });
}

function set_nextcontent(exp, start) {
    var btn_next =  document.getElementById('btn_next');
    $$.data(btn_next, {'exp': exp, 'start': start});
}

function fetch_next() {
    var next_ele =  document.getElementById('btn_next');
    var data = $$.data(next_ele);
    console.log(data);
    var types =$('input[name="group1"]:checked').val()
    var time_start = $('#time_start option:selected').val()
    var time_end = $('#time_end option:selected').val()
    var ldialog = new mdui.Dialog('#loading_dialog', {modal: false});
    ldialog.open();

    var url = "https://api.cn-ki.net/openapi/search?advance=1&&app_id=test&access_token=test&db=SCDB&sort_type=1&keyword=" + data.exp + "&start=" + data.start;
    $.getJSON(url, function (res) {
        console.log(res);
        if (res.status == 0) {
            ldialog.close();
            ldialog.destroy();
            mdui.snackbar({
              message: '发生错误，请稍后再试。'
            });
        }
        var stpl = document.getElementById('resutl_tpl').innerHTML;
        var scontent = template(stpl, {content: res.data.items});
        $('#result_main').append(scontent);
        ldialog.close();
        ldialog.destroy();
        $$('#btn_next_p').show();
        $$('#fab_btn').show();
        set_nextcontent(data.exp, res.data.start);
    });
}

function sort_result(sort_type) {

}

function scroll_top() {
    var position = $('#logo').offset().top;
    console.log(position);
     $('html,body').animate({scrollTop: position}, 1000);
}

function get_db() {
    $('#dbcheck input').each(function () {
        if (this.checked)
            targetdb = $(this).val();
    });
    return targetdb;
}

function get_time_formula() {
    var time_start = $('#time_start').val();
    var time_end = $('#time_end').val();
    var timeexp = " AND ("
    if (time_start == 1 || time_start == '1'){
        return "";
    }else {
        timeexp += "YE BETWEEN ('"+time_start+"','"+time_end+"'))";
        return timeexp;
    }

}

function init_time() {
    // var time_list = [1987, 1988, 1989, 1990, 1991,1992,1993,1994,1995,1996,1997,1998,1999,2000,2001,2002,2003,2004,2005,2006,2007,2008,2009,2010,
    // 2011,2012,2013,2014,2015,2016,2017,2018];
    //
    // for (var i=0; i<time_list.length; i++) {
    //     html = "<option value=\""+time_list[time_list.length - i -1] + "\">"+time_list[time_list.length - i -1]+"</option>";
    //     console.log(html);
    //     $('#time_end').append(html);
    // }
    console.log($('#time_start'));
}