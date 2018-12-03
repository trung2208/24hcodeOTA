$(document).ready(function(){
	$('.waitting').hide();
	$('#google').hide();
	$('#google_mail').hide();
	$('#linux').hide();
	$('#window').hide();
	$('#google_click').click(function(){
	  $("#google").slideToggle(1000);
	});
	$('#google_mail_click').click(function(){
	   $("#google_mail").slideToggle(1000);
	});
	$('#linux_click').click(function(){
	   $("#linux").slideToggle(1000);
	});
	$('#window_click').click(function(){
	   $("#window").slideToggle(1000);
	});
});
     function updateDns(classid){
  			$('.addbinh').each(function(index) {
  			    $(this).hide();
  			});
  			$('.updatebinh').each(function(index) {
  			    $(this).show();
  			});
  			$('.issettruong').each(function(index) {
  			    $(this).hide();
  			});
  			$('.them').each(function(index) {
  			    $(this).show();
  			});
  			$('.add_' + classid).show();
  			$('.update_' + classid).hide();
	  	}
  		function issetDns(){
  			
  			$('.issettruong').each(function(index) {
  			    $(this).show();
  			});
  			$('.addbinh').each(function(index) {
  			    $(this).hide();
  			});
  			$('.updatebinh').each(function(index) {
  			    $(this).show();
  			});
  			$('.them').each(function(index) {
  			    $(this).hide();
  			});
  			
	  	}	
  		function huyDns(){
  			
  			$('.issettruong').each(function(index) {
  			    $(this).hide();
  			});
  			
  			$('.them').each(function(index) {
  			    $(this).show();
  			});	
  		}
		function thoiDns(classid){
			$('.add_' + classid).hide();
			$('.update_' + classid).show();
  		}
		function editDns(classid,recode_id){
			var hostName 	=$("#host_name_"+ classid).val(); 
			var recodeType	= recode_id;
			var recValue	= $.trim($(".recValue_"+ classid ).val());
			var mx			= $.trim($(".mxPreference_"+ classid ).val());
			if(mx == ''){
				var mxPreference = 0;
				}else{
				var mxPreference = mx;	
			}

			var msg1 = recordValidate( hostName , recodeType , recValue , mxPreference ) ;
			//check domain, host record if type = cname
			if(recode_id == 30){
				var value = domainName(recValue);
				if(value == false){
					msg1 = "Tên miền không hợp lệ";
				}
				if(hostName == '@'){
					msg1 = "Host record không đúng";
				}
				recValue = recValue.toLowerCase();
			}

			//if type = text thì substring
			if(recodeType == 50){
				var length = recValue.length;
				var status = recValue.indexOf('\"');
				var length1 = length - 1;

				if(255 < length ){
					var recValue1 = recValue.substring(0,255);
					var recValue2 = recValue.substring(255,length);
					var recValueq = recValue1 + '""' + recValue2;
					var recValue = recValueq;
				}
			}

			//check value if type = mx
			/*if(recodeType == 40){
				//console.log(typeof recValue, "recValue");
				var value = domainName(recValue);
				if(value == false){
					msg1 = "Value của type MX không đúng";
				}
			}
            */
			if(msg1 == false){
				$('.waitting').show();
				$('.DnsSetting').hide();
				$('.end_content').hide();
				$.ajax({
					  type: "POST",
					  url: SITE_URL+"DnsSettingNew/editDns/"+ Math.random(),
					  data: { id: classid,hostName: hostName, recodeType: recodeType,recValue: recValue,mxPreference: mxPreference }
					}).done(function( msg ) {
						if(msg == 1){
					  		var url = SITE_URL+"DnsSettingNew/index";   
							$(location).attr('href',url);
						} else if(msg == 3){
							alert("Bản ghi đã tồn tại");
							var url = SITE_URL;   
							$(location).attr('href',url);
						}else if(msg == 6){
							alert("Value của type A không đúng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 7){
							alert("Value của type MX không đúng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 8){
							alert("Tên miền không hợp lệ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 13){
							alert("Value cho TXT không được bắt đầu và kết thúc bằng kí tự \" ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 14) {
							alert("Không được tồn tại cùng 2 type FRAME và REDIRECT");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 15) {
							alert("Không đúng định dạng IPV6");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else {
							alert("Thiết lập thất bại ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						}
					});
				}
				else{
					alert(msg1);
					var url = SITE_URL;   
					$(location).attr('href',url);
			}
			
  		}	
		function deteleDns(classid, recodeTypeId){
			//alert(classid+hostName+recodeTypeId+recValue+mxPreference);
			$('.addbinh').each(function(index) {
  			    $(this).hide();
  			});
			$('.issettruong').each(function(index) {
  			    $(this).hide();
  			});
			 var x =  confirm('Bạn có muốn xóa bản ghi này không');
			 if (x == true){
				 $('.waitting').show();
				 $('.DnsSetting').hide();
				 $('.end_content').hide();
				$.ajax({
				  type: "POST",
				  url: SITE_URL+"DnsSettingNew/deteleDns/"+ Math.random(),
				  data: { id: classid, recodeType: recodeTypeId }
				}).done(function( msg ){
					if(msg == 1){
				  		var url = SITE_URL;   
						$(location).attr('href',url);
					}else{
						alert("Thiết lập thất bại ");
						var url = SITE_URL;   
						$(location).attr('href',url);
						}
				});
			 }else{
				 var url = SITE_URL;   
					$(location).attr('href',url);
				 }
			
  		}

		function themDns(){
			
			var hostName 		= $.trim($(".hostName").val()); 
			var recodeType		= $.trim($("#recodeType").val());
			var recode		    = $.trim($("#recodeType option:selected").text());
			var recValue		= $.trim($(".recValue").val());
			var mx	            = $.trim($(".mxPreference").val());
			var flg = 0;
			var msg = '';

			$("#dnsNew :input[class='host_name']").each(function(i,elemt){
				//console.log(i);
				//console.log(elemt);
				var bid = (elemt.id) ; // button ID 
				var str = $.trim($("#"+bid).val());
				var res = str.split("_");
				//console.log(res); 
				var host =  res[0];
				var host2 = res[1];
			//console.log(recode);
			//console.log(host2);
				if(host == hostName && host2 != recode && host2 == 'CNAME'){
					flg = 1;
				}
			})

			//check host record 
			if(hostName != '')
			{
				if(recodeType != 50&&recodeType != 80)
				{
					if(recodeType==20) // A
					{
						$fitst_stra = hostName.substring(0, 2);
						if($fitst_stra=="*.")
						{
							hostName_h1 = hostName.substring(2);
							var value12345 = hostRecord(hostName_h1);
							if(value12345 == false)
							{
								msg = "Host record không đúng định dạng";
								alert(msg);return;
							}
						}
					}
					else
					{
						var value = hostRecord(hostName);
						if(value == false){
							msg = "Host record không đúng định dạng...";
							alert(msg);return;
						}
					}
				}
			} 

			//if type = text thì substring
			if(recodeType == 50){
				var length = recValue.length;
				var status = recValue.indexOf('\"');
				var length1 = length - 1;
				
				if(255 < length ){
					var recValue1 = recValue.substring(0,255);
					var recValue2 = recValue.substring(255,length);
					var recValueq = recValue1 + '""' + recValue2;
					var recValue = recValueq;
				}
			}

			//console.log(flg);
			if(flg != 1){
				if(mx == ''){
					var mxPreference = 0;
					}else{
					var mxPreference = mx;	
				}
				var msg = recordValidate( hostName , recodeType , recValue , mxPreference ) ;
			} else {
				msg = 'Host record '+ hostName + ' đã được sử dụng ở bản ghi khác';
			}

			//check domain, host record if type = cname
			if(recodeType == 30){
				var value = domainName(recValue);
				if(value == false){
					msg = "Tên miền không hợp lệ";
				}
				if(hostName == '@'){
					msg = "Host record không đúng";
				}
				recValue = recValue.toLowerCase();
			}

			//check value if type = mx
			/*if(recodeType == 40){
				//console.log(typeof recValue, "recValue");
				var value = domainName(recValue);
				if(value == false){
					msg = "Value của type MX không đúng";
				}
			}
             */
			if( msg == ''){
				$('.waitting').show();
				$('.DnsSetting').hide();
				$('.end_content').hide();
				$.ajax({
					  type: "POST",
					  url: SITE_URL+"DnsSettingNew/addDns/"+ Math.random(),
					  data: { hostName: hostName, recodeType: recodeType,recValue: recValue,mxPreference: mxPreference }
					}).done(function( msg ) {
						if(msg == 1){
					  		var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 3){
							alert("Bản ghi đã tồn tại");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 4){
							alert("Host record bản ghi Txt không chứa khoảng trắng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 5){
							alert("Bản ghi  MX đã tồn tại");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 6){
							alert("Value của type A không đúng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 7){
							alert("Value của type MX không đúng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 8){
							alert("Tên miền không hợp lệ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 9){
							alert("Host record không đúng định dạng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 10) {
							alert("Host recod cho CNAME đã tồn tại");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 11) {
							alert("CNAME và A không được cùng 1 host recod");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 12) {
							alert("Host recod cho CNAME không đúng");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 13) {
							alert("Value cho TXT không được bắt đầu và kết thúc bằng kí tự \" ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 14) {
							alert("Không được tồn tại cùng 2 type FRAME và REDIRECT");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else if(msg == 15) {
							alert("Không đúng định dạng IPV6");
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else{
							alert("Thiết lập thất bại ");
							var url = SITE_URL;   
							$(location).attr('href',url);
						}
				});
			}else{
				alert(msg);
			}

  		}	
  		function deteleAll(){
    	  if( $('input[type=checkbox]:checked').val() == undefined){
    		  alert ('bạn chưa chon');  
        	  }else{
        		 var x =  confirm('Bạn có muốn xóa bản ghi này không');
      			 if (x == true){
       					$("#dnsNew").submit();
       					$('.waitting').show();
    					$('.DnsSetting').hide();
    					$('.end_content').hide();
          			 }else{
         				  alert ('khong xoa'); 
              		 }
           }
  	  	}	
  		function defaulGoogle(){
  			var r = confirm("Sử dụng Default Google Site tất cả các bản ghi cũ sẽ bị xóa hết");
  			if (r == true) {
	  	  		var dnsdefaulgoogle = 'google';
	  	  	  	$('.waitting').show();
	  			$('.DnsSetting').hide();
	  			$('.end_content').hide();
	  			$.ajax({
					  type: "POST",
					  url: SITE_URL+"DnsDomainDefaul/issetDefaultGoogleDns/"+ Math.random(),
					  data: { dnsdefaul:dnsdefaulgoogle}
					}).done(function( msg ) {
						if(msg > 0){
					  		var url =SITE_URL;   
							$(location).attr('href',url);
						}else{
							alert('Thiết lập bản ghi thất bại');
							var url = "{$smarty.const.SITE_URL}";   
							$(location).attr('href',url);
						}
				});
  			}  else {
  				var url = SITE_URL;   
				$(location).attr('href',url);
  	  		}
      	 }
  		function defaulGoogleMail(){
  			var r = confirm("Sử dụng Default Google Mail tất cả các bản ghi cũ sẽ bị xóa hết");
  			if (r == true) {
	  			$('.waitting').show();
				$('.DnsSetting').hide();
				$('.end_content').hide();
	  	  		var dnsdefaulgoogle = 'googlemail';
	  			$.ajax({
					  type: "POST",
					  url: SITE_URL+"DnsDomainDefaul/issetDefaultGoogleMailDns/"+ Math.random(),
					  data: { dnsdefaul:dnsdefaulgoogle}
					}).done(function( msg ) {
						if(msg > 0){
					  		var url = SITE_URL;   
							$(location).attr('href',url);
						}else{
							alert('Thiết lập bản ghi thất bại');
							var url = SITE_URL;   
							$(location).attr('href',url);
						}
				});
  			} else {
  				var url = SITE_URL;   
				$(location).attr('href',url);
  	  	  	}	
      	 }
  		function defaulHosting(){
  			var r = confirm("Sử dụng Default Hosting tất cả các bản ghi cũ sẽ bị xóa hết");
  			if (r == true) {
	  	  		var dnsdefaul = 'hosting';
	  	  	  	$('.waitting').show();
	  			$('.DnsSetting').hide();
	  			$('.end_content').hide();
	  			$('.content_row1').hide();
	  			$.ajax({
					  type: "POST",
					  url: SITE_URL+"DnsDomainDefaul/addDnsHosting/"+ Math.random(),
					  data: { dnsdefaul:dnsdefaul}
					}).done(function( msg ) {
						
						if(msg > 0){
					  		var url = SITE_URL;   
							$(location).attr('href',url);
						}else if(msg == 0 ) {
							alert('Thiết lập bản ghi thất bại');
							var url = SITE_URL;   
							$(location).attr('href',url);
						} else {
							alert('Bạn chưa có hosting tại tenten');
							var url = SITE_URL;   
							$(location).attr('href',url);
							}
				});
  			} else {
	  			var url = SITE_URL;   
				$(location).attr('href',url);
	  	  	  }
  }

  function domainName(value) {
	var regex = /^(?!-)([\w\d]+([\-]{0,1}[\w\d]){0,}\.)+[\w]{2,6}$/;
	var key = value;


	if (!regex.test(key)) {
	    return false;
	}
	return true;
}

function hostRecord(value) {
    var regex = /^((?!-)([\w\d]+[\-][\w\d]+){1,63}|[\@]|[\w]+|[\*]|(?!-)([\w\d]+([\-]{0,1}[\w\d]){0,}\.)+[\-\_\w]{2,100})$/;
    var key = value;

    if (!regex.test(key)) {
        return false;
    }
    return true;
}
