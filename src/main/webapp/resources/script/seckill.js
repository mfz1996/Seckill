//存放主要交互逻辑js代码
var seckill = {
    //封装秒杀相关ajax的url
    URL: {
        now : function () {
            return '/seckill/time/now';
        },
        exposer : function (seckillId) {
            return '/seckill/'+seckillId+'/exposer';
        },
        excution : function (seckillId,md5) {
            return '/seckill/'+seckillId+'/'+md5+'/excution';
        }
    },
    //验证手机号
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    handerSeckill : function(seckillId,node){
        //处理秒杀逻辑
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId),{},function (result) {
            if (result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']) {
                    //开启秒杀，获取秒杀地址
                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.excution(seckillId,md5);
                    console.log('killUrl:'+killUrl);
                    $('#killBtn').one('click',function (seckillId,md5) {
                        $(this).addClass('disabled');
                        $.post(killUrl,{},function (result) {
                            if (result && result['success']){
                                var killResult = result['data'];
                                var state = killResult['state'];
                                var stateInfo = killResult['stateInfo'];
                                node.html('<span class="label label-success">'+stateInfo+'</span>');
                            }
                        });
                    });
                    node.show();
                }else {
                    //未开启秒杀（客户端与服务器计时差异问题）
                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    //重新倒计时
                    seckill.timecountdown(seckillId,now,start,end);
                }
            }else {
                console.log('result:'+result)
            }
        })

    },
    timecountdown : function(seckillId,nowTime,startTime,endTime){
        var seckillBox = $('#seckill-box');
        if (nowTime>endTime){
            seckillBox.html('<h2>秒杀结束！</h2>');
        }else if(nowTime<startTime){
            var killTime = new Date(startTime + 1000);
            seckillBox.countdown(killTime, function (event) {
                var format = event.strftime('<span></span>秒杀倒计时：%D天 %H时 %M分 %S秒');
                seckillBox.html(format);

            }).on('finish.countdown',function () {
                //倒计时完成，获取秒杀地址，控制显示逻辑，执行秒杀
                seckill.handerSeckill(seckillId,seckillBox);
            });
        }else {
            //秒杀开始
            seckill.handerSeckill(seckillId,seckillBox);

        }
    },
    //详情页秒杀逻辑
    detail: {
        init: function (params) {
            //用户手机验证和登录，计时交互
            //规划交互流程
            //在cookie中查找手机号
            var killPhone = $.cookie('killPhone');


            //验证手机号
            if (!seckill.validatePhone(killPhone)) {
                //绑定phone
                //控制输出
                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $('#killPhoneBtn').click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    console.log('inputPhone = '+inputPhone);//TODO
                    if (seckill.validatePhone(inputPhone)) {
                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill'});
                        window.location.reload();
                    } else {
                        $('#killPhoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
                    }
                });

            }
            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            $.get(seckill.URL.now(),{},function (result) {
                if (result && result['success']){
                    var nowTime = result['data'];
                    seckill.timecountdown(seckillId,nowTime,startTime,endTime);
                } else {
                    console.log('result:'+result)
                }
            })
        }
    }
}