//
//  BarcodeScannerViewController.h
//  barcode
//
//  Created by jingren on 16/9/11.
//  Copyright © 2016年 jieweifu. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol BarcodeScannerDelegate <NSObject>

@optional

#pragma mark - Listening for Reader Status

- (void)reader:(NSDictionary *)result;
/**
 * @abstract Tells the delegate that the user wants to stop scanning QRCodes.
 * @param reader The reader view controller that the user wants to stop.
 * @since 1.0.0
 */
- (void)readerDidCancel;

@end

@interface BarcodeScannerViewController : UIViewController
@property (nonatomic, weak) id<BarcodeScannerDelegate> delegate;
@property (weak, nonatomic) IBOutlet UILabel *firstSubLabel;
@property (weak, nonatomic) IBOutlet UILabel *secondSubLabel;

@property (nonatomic,strong) NSDictionary *requestDic;
@property (nonatomic,strong) NSString *firstString;
@property (nonatomic,strong) NSString *secondString;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scanWidthConstraint;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *scanHeightConstraint;

@property (nonatomic,strong) NSDictionary *messageDic;//显示信息
@property (nonatomic,strong) NSString *operationType;//处理类型
@property (nonatomic,strong) NSArray *requestList;//http请求列表

@end


