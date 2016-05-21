/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */

import React, { Component } from 'react';
import {
    AppRegistry,
    BackAndroid,
    Navigator,
    StyleSheet,
    TouchableOpacity,
    Text,
    View
} from 'react-native';

import XTextView from './app/components/views/XTextView';
import Web from './app/pages/webPage';

var thisNavigator, isRemoved = true;

class Home extends Component {
    // 构造
    constructor(props) {
        super(props);
        // 初始状态
        this.state = {};
    }

    render() {
        const {navigator} = this.props;
        return (
            <View style={[{flex: 1}]}>
                <Text
                    style={[{backgroundColor: '#ff6600', color: 'black', height: 100}]}
                    onPress={()=>{}}>
                    ok! Welcome to Images!
                </Text>
                <Text
                    style={[{backgroundColor: '#66ff66', color: 'black', height: 100}]}
                    onPress={()=>{}}>
                    ok! Welcome to List!
                </Text>
                <Text
                    style={[{backgroundColor: '#6666ff', color: 'black', height: 100}]}
                    onPress={()=>{navigator.push({name:'webview', container: Web})}}>
                    ok! Welcome to Web!
                </Text>
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

class testWeb extends Component {
    // 构造
    constructor(props) {
        super(props);
        // 初始状态
        this.state = {};
    }

    _goBack() {
        if (thisNavigator && thisNavigator.getCurrentRoutes().length > 1) {
            thisNavigator.pop();
        } else {
            return false;
        }
        return true;
    }

    _renderScene(route, navigator) {
        thisNavigator = navigator;
        let Container = route.container;
        if (route.name === 'WebViewPage') {
            BackAndroid.removeEventListener('hardwareBackPress', this._goBack);
            isRemoved = true;
        } else {
            if (isRemoved) {
                BackAndroid.addEventListener('hardwareBackPress', this._goBack);
            }
        }
        return <Container navigator={navigator} {...route.params}/>
    };

    render() {
        var sceneConfig = Object.assign({}, Navigator.SceneConfigs.FloatFromRight, {gestures: {pop: null}});

        return (
            <Navigator
                initialRoute={{name: 'home', container: Home}}
                configureScene={(route)=>sceneConfig}
                renderScene={this._renderScene.bind(this)}/>
        );
    }
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

AppRegistry.registerComponent('testWeb', () => testWeb);
