"use strict";
import React, {PropTypes} from 'react';
import {requireNativeComponent} from 'react-native';

//var RCTAdvancedWebView = requireNativeComponent('RCTAdvancedWebView', AdvancedWebView);

var iface = {

    name: 'AdvancedWebView',

    propTypes: {
        url: PropTypes.string,
        html: PropTypes.string,
        textZoom: PropTypes.number,
    },

};

module.exports = requireNativeComponent('RCTAdvancedWebView', iface);

/*
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

//var RCTAdvancedWebView = requireNativeComponent('RCTAdvancedWebView', WebView,{
//    nativeOnly: {onChange: true}
//});

*/
