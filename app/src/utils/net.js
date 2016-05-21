/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, { Component} from 'react';

import {
    AppRegistry,
} from 'react-native';

export default class Net extends Component {
//post请求
    static  postForm(url, data, callback) {
        var fetchOptions = {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body:'data='+data+''
        };

        fetch(url, fetchOptions)
            .then((response) => response.text())
            .then((responseText) => {
                callback(JSON.parse(responseText));
            }).done();
    }
    //
    static postJson (url, data, callback) {
        var fetchOptions = {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                //json形式
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        };

        fetch(url, fetchOptions)
            .then((response) => response.text())
            .then((responseText) => {
                callback(JSON.parse(responseText));
            }).done();
    }


    //get请求
    static  get(url, callback) {
        fetch(url)
            .then((response) => response.text())
            .then((responseText) => {
                callback(JSON.parse(responseText));
            }).done();
    }

    log(obj){
        var description = "";
        for(var i in obj){
            var property=obj[i];
            description+=i+" = "+property+"\n";
        }
        alert(description);
    }

    /*static  url_healthmonitbase(){
        const time= Util.getTime();
        const s=  Global.HOST+"/healthmonitbase?customerid=0010000022464&density=480&ko=0000&time="+time+"&token="+Util.getToken(time);
        return s ;
    }

    static url_healthmonitnorm(checkItemCode){
        const time= Util.getTime();
        const  ul = Global.HOST+"/healthmonitnorm?checkItemCode=" + checkItemCode +"&customerid=0010000022464&sexid=1&currentPage=1&density=480&ko=0000&time="+time+"&token="+Util.getToken(time);
        return ul;
    }*/

}