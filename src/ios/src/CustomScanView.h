//
//  CustomScanView.h
//  HandTest
//
//  Created by Mr.xiao on 17/3/15.
//
//

#import <UIKit/UIKit.h>

@interface CustomScanView : UIView

@property (nonatomic,strong) UIImageView *imgView;
@property (nonatomic,strong) UILabel *msgLabel;
@property (nonatomic,strong) UIButton *openBtn;

@property (nonatomic,strong) UILabel *firstLabel;
@property (nonatomic,strong) UILabel *secondLabel;

@end


@interface NetWorkView : UIView

@property (nonatomic,strong) UIImageView *imgView;
@property (nonatomic,strong) UILabel *msgLabel;

@end


@interface NetWorkTool : NSObject

+ (instancetype)shareInstance;

+ (NSURLSessionDataTask *)request:(NSString *)url header:(NSDictionary *)header method:(NSString *)method params:(NSDictionary *)params success:(void (^)(id response))successBlock failure:(void(^)(NSError *error))failure;

@end
