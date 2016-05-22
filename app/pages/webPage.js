/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, { Component } from 'react';
import {
    Navigator,
    StyleSheet,
    TouchableOpacity,
    Text,
    View
} from 'react-native';

//import AdvancedWebView from '../components/views/XAdvancedWebView';
import AdvancedWebView from '../components/views/XWeb';
import XTextView from '../components/views/XTextView';

export default class Web extends Component {
    // 构造
    constructor(props) {
        super(props);
        // 初始状态
        this.state = {
            title: 'Html',
            source: {uri: ''},
        };
    }

    _onPressUri() {
        //if (type === 1) {
            this.setState({source: {uri: 'http://www.jcczgb.com/wap_new/tmpl/member/userinfo.html'}})
        //} else {
        //    this.setState({source: require('./app/src/main.html')});
        //}
    }
    _onPressHtml(){
        //if (type === 1) {
        //    this.setState({source: {uri: 'http://www.jcczgb.com/wap_new/tmpl/member/userinfo.html'}})
        //} else {
            this.setState({source: require('../src/main.html')});
        //}
    }
    _onWebViewMessage(message) {
        ToastAndroid.show(message, ToastAndroid.SHORT);
        let msg = JSON.parse(message);
        //dispatch(webActions.setText(msg.name));
        const { webviewbridge } = this.refs;
        switch (message) {
            case "hello from webview":
                webviewbridge.sendToBridge("hello from react-native");
                break;
            case "got the message inside webview":
                this.setState({title: msg.name});
                ToastAndroid.show(message, ToastAndroid.SHORT);
                console.log("we have got a message from webview! yeah");
                break;
            default:
                break;
        }
    }

    render() {
        const injectScript = `
        function share(){
            var ret = prompt("JsBridge", getShareJson());
            document.getElementById("input2").value = ret + count++;
        }
        var btn = document.getElementById("btn");
        btn.onclick=share;
        btn.value="sssssss";
        `;
        let {source, title} = this.state;
        return (
            <View style={styles.container}>
                <View style={[{flexDirection:'row', height: 40, width: 360}]}>
                    <TouchableOpacity style={[{flex: 1}]}
                                      onPress={this._onPressHtml.bind(this)}>
                        <Text style={[styles.welcome, {flex: 1}]}>
                            {title}
                        </Text>
                    </TouchableOpacity>
                    <TouchableOpacity  style={[{flex: 1}]}
                                       onPress={this._onPressUri.bind(this)}>
                        <Text style={[styles.welcome, {flex: 1}]}>
                            To Uri
                        </Text>
                    </TouchableOpacity>
                </View>
                <View style={styles.container}>
                    <AdvancedWebView style={{height:560, width: 360}}
                        ref="webviewbridge"
                        onWebViewMessage={this._onWebViewMessage}
                        source={source}
                        injectedJavaScript={injectScript}
                        javaScriptEnabled={true}
                        domStorageEnabled ={true}
                    />
                </View>
            </View>
        );
    }

    //  source={{uri:'http://www.jcczgb.com/wap_new/tmpl/member/userinfo.html'}}
    //  url={'http://www.jcczgb.com/wap_new/tmpl/member/userinfo.html'}
//<XTextView style={{height:40, width: 300}}
//text={this.state.title}
//texSize={38}
///>
}
const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: '#F5FCFF',
    },
    welcome: {
        fontSize: 20,
        textAlign: 'center',
        backgroundColor: '#33ddFF',
        margin: 0,
    },
    instructions: {
        textAlign: 'center',
        color: '#333333',
        marginBottom: 5,
    },
});

