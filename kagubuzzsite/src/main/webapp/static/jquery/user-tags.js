function userTagsInit() {
	
var discussion_tags = $.cookie('discussion_search_tags');

var tagList = $.parseJSON(discussion_tags);

jQuery(".tagManager:eq(0)").tagsManager({
    preventSubmitOnEnter: true,
    prefilled: tagList,
    typeahead: true,
    typeaheadSource: typeAheadHelper,
    blinkBGColor_1: '#FFFF9C',
    blinkBGColor_2: '#CDE69C',
    hiddenTagListName: 'tagList',
  });

}

function notifyRemoved(name){
     var tagList = $.parseJSON($.cookie('discussion_search_tags'));
     
     tagList.splice(tagList.indexOf(name),1);
     
     if(tagList.length == 0) tagList = null;

     $.cookie('discussion_search_tags', JSON.stringify(tagList), { expires: 365, path: '/' });
}

function notifyAdded(name) { 
    
    var result = jQuery(".tagManager:eq(0)").tagsManager('pushTag',name);
    
    var tagList = $.parseJSON($.cookie('discussion_search_tags'));
    
    if(tagList  == null) tagList = new Array();
    
    if(tagList.indexOf(name) !== -1) return;
    
    if(tagList.length + 1 > 5) {
    	renderServerNotification( JSON.parse('${max_tags_message}'));
    	return;
    }
    
    tagList.push(name);
    
    $.cookie('discussion_search_tags', JSON.stringify(tagList), { expires: 365 , path: '/' });
}