
function sortResult(type, kw, db) {
    var p = '1';
    window.location.href = 'search?keyword=' + kw + "&db=" + db + "&p=" + p + "&s_type=" + type;
}

function searchByTitle(kw) {
    url = 'search?keyword=' + kw + "&db=SCDB&p=1" + "&s_type=2";
    window.location.href = url;
}

function changeDb(db) {

    var dbMap = {
        'db1': 'CFLS',
        'db2': '2',
        'db3': '3',
        'db4': '4',
        'db5': 'CCND'
    };

    for(var prop in dbMap){
        if(dbMap.hasOwnProperty(prop)){
            if (prop != db) {
                $('#'+prop).removeAttr('class');
            }
        }
    }

    $('#'+db).attr('class', 'mdui-color-blue mdui-text-color-white');
    $('#searchDb').val(function (index, value) {
        console.log(value);
        return dbMap[db];
    });
    debugger
    var keyword = $('#keyword').val();
    if (keyword.length > 0) {
        console.log(keyword);
        url = 'search?keyword=' + keyword + '&type=' + dbMap[db];
        window.location.href = url;
    }
}

function changeIndexDb(db) {
    removeAdvanceSearch();
    removeSearchByTitle();

    var dbMap = {
        'db1': 'CFLS',
        'db2': '2',
        'db3': '3',
        'db4': '4',
        'db5': 'CCND'
    };

    for(var prop in dbMap){
        if(dbMap.hasOwnProperty(prop)){
            if (prop != db) {
                $('#'+prop).attr('class', 'item');
            }
        }
    }

    $('#'+db).attr('class', 'item cur');
    console.log(dbMap[db])
    $('#searchDb').val(dbMap[db]);

    var keyword = $('#keyword').val();

}

function setAdvanceSearch(ele) {
    var dbMap = {
        'db1': 'CFLS',
        'db2': 'CFLQ',
        'db3': 'CDMD',
        'db4': 'CIPD',
        'db5': 'CCND'
    };

    for(var prop in dbMap){
        if(dbMap.hasOwnProperty(prop)){
            $('#'+prop).attr('class', 'item');
        }
    }
    $(ele).attr('class', 'item cur');
    $('#advance_search').remove();
    removeSearchByTitle();

    advance_node = '<input hidden id="advance_search" name="advance" value="1"/>';
    $('#searchForm').append(advance_node);
}

function setSearchByTitle(ele) {
    var dbMap = {
        'db1': 'CFLS',
        'db2': 'CFLQ',
        'db3': 'CDMD',
        'db4': 'CIPD',
        'db5': 'CCND'
    };

    for(var prop in dbMap){
        if(dbMap.hasOwnProperty(prop)){
            $('#'+prop).attr('class', 'item');
        }
    }
    $(ele).attr('class', 'item cur');
    $('#s_type').remove();
    removeAdvanceSearch();
    //移除之前的标志ok
    s_type_node = '<input hidden id="s_type" name="s_type" value="3"/>';
    $('#searchForm').append(s_type_node);
}

function removeAdvanceSearch() {
    $('#highSearch').attr('class', '');
    $('#advance_search').remove();
}

function removeSearchByTitle() {
    $('#searchByTitle').attr('class', '');
    $('#s_type').remove();
}

function search(ele) {



}

function getRelated(keyword) {
    url = "https://www.cn-ki.net/kns/request/NewGetRelavantHandler.ashx?action=relevant&pagename=ASP.brief_default_result_aspx&dbPrefix=SCDB&dbCatalog=%E4%B8%AD%E5%9B%BD%E5%AD%A6%E6%9C%AF%E6%96%87%E7%8C%AE%E7%BD%91%E7%BB%9C%E5%87%BA%E7%89%88%E6%80%BB%E5%BA%93&ConfigFile=SCDBINDEX.xml&research=off&keyValue="+ keyword + "&S=1&dbcode=SCDB&selectedField=%E4%B8%BB%E9%A2%98";
    
    $.get(url, function (data) {
        info = $.parseJSON(data);

        kw_list = info.keyWordJson.split('|');
        console.log(kw_list);

        target_node = $('#related');
        $.each(kw_list,function(index,value,list){
            html = '<div class="mdui-col"><a href="search?keyword='+value+'">'+ value +'</a></div>';
            target_node.append(html);
    　   })
    })
}
