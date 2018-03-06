//
//  CDVBarcodeScanner.h
//  HelloCordova
//
//  Created by jingren on 16/9/11.
//
//

#import <Cordova/CDV.h>
#import "BarcodeScannerViewController.h"

@interface CDVBarcodeScanner : CDVPlugin<BarcodeScannerDelegate>

@property (nonatomic, strong) NSString *currentCallbackId;
- (void)startScan:(CDVInvokedUrlCommand *)command;

- (void)hasPermission:(CDVInvokedUrlCommand *)command;
@end
