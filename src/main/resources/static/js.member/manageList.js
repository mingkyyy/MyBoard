$(function (){
   $('.report').click(function (){
       let id = $(this).val();

       if (confirm("신고 확인 완료 하였습니까?")){
           $.ajax({
               type: 'DELETE',
               url: '/report/'+id,
               dataType:'json',
           }).done(function (){
               alert("삭제 완료 되었습니다.");
               window.location.href = '/manage';
           }).fail(function (error){
               alert(JSON.stringify(error));
           })
       }else {

       }
   })
})