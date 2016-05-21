"use strict";
import React, {PropTypes} from 'react';
import {requireNativeComponent, View} from 'react-native';

//var requireNativeComponent = require('requireNativeComponent');

var iface = {
    name: 'XTextView',
    propTypes: {
        ...View.propTypes,
        text: PropTypes.string,
        textSize: PropTypes.number,
        textColor: PropTypes.number,
        isAlpha: PropTypes.bool,

    },
};

module.exports = requireNativeComponent('RCTTextView', iface);
/*

var RCTAdvancedWebView = requireNativeComponent('RCTAdvancedWebView', AdvancedWebView);
export default class AdvancedWebView extends React.Component {
    constructor() {
        super();
        //this._onChange = this._onChange.bind(this);
    }

    /!* _onChange(event: Event) {
     if (!this.props.onScrollChange) {
     return;
     }
     this.props.onScrollChange({ScrollX:event.nativeEvent.ScrollX,ScrollY:event.nativeEvent.ScrollY});
     }*!/

    render() {
        ToastAndroid.show('RCT render', ToastAndroid.SHORT);

        return <RCTAdvancedWebView {...this.props} />
        //return <RCTAdvancedWebView {...this.props} onChange={this._onChange} />
    }
}

AdvancedWebView.propTypes = {
    url: PropTypes.string,
    html: PropTypes.string,
    textZoom: PropTypes.number,
    //onScrollChange: PropTypes.func,
};
*/

//var RCTAdvancedWebView = requireNativeComponent('RCTAdvancedWebView', WebView,{
//    nativeOnly: {onChange: true}
//});

