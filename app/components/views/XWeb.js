/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * Copyright (c) 2016-present, Ali Najafizadeh
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 *
 * @providesModule WebViewBridge
 */
"use strict";
import React, {PropTypes} from 'react';
//import {requireNativeComponent, ToastAndroid, WebView} from 'react-native';

var invariant = require('invariant');
var keyMirror = require('keymirror');
var resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');
//: {
//    WebViewBridgeManager
//}
import {
    ReactNativeViewAttributes,
    UIManager,
    EdgeInsetsPropType,
    StyleSheet,
    Text,
    View,
    WebView,
    requireNativeComponent,
    DeviceEventEmitter,
    NativeModules,
    } from 'react-native';

var RCT_WEBVIEWBRIDGE_REF = 'webviewbridge';

var WebViewBridgeState = keyMirror({
    IDLE: null,
    LOADING: null,
    ERROR: null,
});

/**
 * Renders a native WebView.
 */


class XWebViewBridge extends React.Component {

    // 构造
      constructor(props) {
        super(props);
        // 初始状态
          this.injectBridgeScript = this.injectBridgeScript.bind(this);
          this.injectBridgeScript = this.injectBridgeScript.bind(this);
        this.state = {
            viewState: WebViewBridgeState.IDLE,
            lastErrorEvent: null,
            startInLoadingState: true,};
      }

    componentWillMount() {
        DeviceEventEmitter.addListener("onMessage", (body) => {
            const { onWebViewMessage } = this.props;
            const message = body.message;
            if (onWebViewMessage) {
                onWebViewMessage(message);
            }
        });

        if (this.props.startInLoadingState) {
            this.setState({viewState: WebViewBridgeState.LOADING});
        }
    }

    render() {
        var otherView = null;
        otherView = <Text >ABCD</Text>;
        /*

        if (this.state.viewState === WebViewBridgeState.LOADING) {
            otherView = this.props.renderLoading && this.props.renderLoading();
        } else if (this.state.viewState === WebViewBridgeState.ERROR) {
            var errorEvent = this.state.lastErrorEvent;
            otherView = this.props.renderError && this.props.renderError(
                    errorEvent.domain,
                    errorEvent.code,
                    errorEvent.description);
        } else if (this.state.viewState !== WebViewBridgeState.IDLE) {
            console.error('RCTXAdvancedWebView invalid state encountered: ' + this.state.loading);
        }
         */

        var webViewStyles = [styles.container, this.props.style];
        if (this.state.viewState === WebViewBridgeState.LOADING ||
            this.state.viewState === WebViewBridgeState.ERROR) {
            // if we're in either LOADING or ERROR states, don't show the webView
            webViewStyles.push(styles.hidden);
        }

        var {javaScriptEnabled, domStorageEnabled} = this.props;
        if (this.props.javaScriptEnabledAndroid) {
            console.warn('javaScriptEnabledAndroid is deprecated. Use javaScriptEnabled instead');
            javaScriptEnabled = this.props.javaScriptEnabledAndroid;
        }
        if (this.props.domStorageEnabledAndroid) {
            console.warn('domStorageEnabledAndroid is deprecated. Use domStorageEnabled instead');
            domStorageEnabled = this.props.domStorageEnabledAndroid;
        }
        let {source, ...props} = {...this.props};

        let uri = resolveAssetSource(source);
        //ToastAndroid.show('uri: '+uri, ToastAndroid.SHORT);


        var webView =
            <RCTAdvancedWebView
                ref={RCT_WEBVIEWBRIDGE_REF}
                key="webViewKey"
                {...props}
                source={resolveAssetSource(source)}
                style={webViewStyles}
                onLoadingStart={this.onLoadingStart}
                onLoadingFinish={this.onLoadingFinish}
                onLoadingError={this.onLoadingError}
            />;

        return (
            <View style={styles.container}>
                {webView}
                {otherView}
            </View>
        );
    }

    goForward () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewBridgeHandle(),
            UIManager.RCTXAdvancedWebView.Commands.goForward,
            null
        );
    }

    goBack () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewBridgeHandle(),
            UIManager.RCTXAdvancedWebView.Commands.goBack,
            null
        );
    }

    reload () {
        UIManager.dispatchViewManagerCommand(
            this.getWebViewBridgeHandle(),
            UIManager.RCTXAdvancedWebView.Commands.reload,
            null
        );
    }

    sendToBridge (message:string) {
        ToastAndroid.show('sendToBridge'+message, ToastAndroid.SHORT);
        UIManager.dispatchViewManagerCommand(
            this.getWebViewBridgeHandle(),
            UIManager.RCTXAdvancedWebView.Commands.sendToBridge,
            [message]
        );
    }

    injectBridgeScript () {
        ToastAndroid.show('injectBridgeScript', ToastAndroid.SHORT);
        UIManager.dispatchViewManagerCommand(
            this.getWebViewBridgeHandle(),
            UIManager.RCTXAdvancedWebView.Commands.injectBridgeScript,
            null
        );
    }

    /**
     * We return an event with a bunch of fields including:
     *  url, title, loading, canGoBack, canGoForward
     */
    updateNavigationState (event) {
        if (this.props.onNavigationStateChange) {
            this.props.onNavigationStateChange(event.nativeEvent);
        }
    }

    getWebViewBridgeHandle () {
        return React.findNodeHandle(this.refs[RCT_WEBVIEWBRIDGE_REF]);
    }

    onLoadingStart (event) {
        this.injectBridgeScript();
        var onLoadStart = this.props.onLoadStart;
        onLoadStart && onLoadStart(event);
        this.updateNavigationState(event);
    }

    onLoadingError (event) {
        event.persist(); // persist this event because we need to store it
        var {onError, onLoadEnd} = this.props;
        onError && onError(event);
        onLoadEnd && onLoadEnd(event);
        console.error('Encountered an error loading page', event.nativeEvent);

        this.setState({
            lastErrorEvent: event.nativeEvent,
            viewState: WebViewBridgeState.ERROR
        });
    }

    onLoadingFinish (event) {
        var {onLoad, onLoadEnd} = this.props;
        onLoad && onLoad(event);
        onLoadEnd && onLoadEnd(event);
        this.setState({
            viewState: WebViewBridgeState.IDLE,
        });
        this.updateNavigationState(event);
    }
};

var styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    hidden: {
        height: 0,
        flex: 0, // disable 'flex:1' when hiding a View
    },
});

XWebViewBridge.propTypes = {
    ...WebView.propTypes,
    /**
     * Will be called once the message is being sent from webview  RCTXAdvancedWebView
     */
    onWebViewMessage: PropTypes.func,
};

var RCTAdvancedWebView = requireNativeComponent('RCTXAdvancedWebView', XWebViewBridge);
module.exports = XWebViewBridge;
