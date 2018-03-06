//
//  CustomScanView.m
//  HandTest
//
//  Created by Mr.xiao on 17/3/15.
//
//

#import "CustomScanView.h"

@implementation CustomScanView

- (instancetype)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        
        [self initSubViews];
    }
    return self;
}

- (void)initSubViews{
    CGSize size = self.frame.size;
    self.backgroundColor = [UIColor colorWithRed:127.0/255.0 green:127.0/255.0 blue:127.0/255.0 alpha:1];
    _imgView = [[UIImageView alloc] initWithFrame:CGRectMake((size.width-200)/2, 30, 200, 200)];
    _imgView.image = [UIImage imageNamed:@"hly_pc"];
    [self addSubview:_imgView];
    
    _msgLabel = [[UILabel alloc] initWithFrame:CGRectMake((size.width-200)/2, CGRectGetMaxY(_imgView.frame)+20, 200, 50)];
    _msgLabel.numberOfLines = 0;
    _msgLabel.text = @"请按照页面下方提示\n先打开汇联易网站";
    _msgLabel.textColor = [UIColor whiteColor];
    _msgLabel.textAlignment = NSTextAlignmentCenter;
    [self addSubview:_msgLabel];
    _openBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    _openBtn.frame = CGRectMake((size.width-140)/2, CGRectGetMaxY(_msgLabel.frame)+20, 140, 35);
    [_openBtn setTitle:@"我已打开" forState:UIControlStateNormal];
    _openBtn.layer.cornerRadius = 15;
    [_openBtn setBackgroundColor:[UIColor colorWithRed:23/255.0 green:147/255.0 blue:215/255.0 alpha:1]];
    [self addSubview:_openBtn];
    
    _secondLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, size.height-25-30, size.width-40, 30)];
    _secondLabel.textColor = [UIColor whiteColor];
    _secondLabel.font = [UIFont systemFontOfSize:15];
    _secondLabel.numberOfLines = 0;
    _secondLabel.textAlignment = NSTextAlignmentCenter;
    [self addSubview:_secondLabel];
    _firstLabel = [[UILabel alloc] initWithFrame:CGRectMake(20, CGRectGetMinY(_secondLabel.frame)-55, size.width-40, 50)];
    _firstLabel.numberOfLines = 0;
    _firstLabel.textColor = [UIColor whiteColor];
    _firstLabel.font = [UIFont systemFontOfSize:15];
    _firstLabel.textAlignment = NSTextAlignmentCenter;
    [self addSubview:_firstLabel];
    
}



@end


@implementation NetWorkView

- (instancetype)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        
        [self initSubViews];
    }
    return self;
}


- (void)initSubViews{
    CGSize size = self.frame.size;
    self.backgroundColor = [UIColor colorWithRed:127.0/255.0 green:127.0/255.0 blue:127.0/255.0 alpha:1];
    _imgView = [[UIImageView alloc] initWithFrame:CGRectMake((size.width-200)/2, 50, 200, 200)];
    _imgView.image = [UIImage imageNamed:@"hly_net"];
    [self addSubview:_imgView];
    
    _msgLabel = [[UILabel alloc] initWithFrame:CGRectMake((size.width-200)/2, 275, 200, 50)];
    _msgLabel.numberOfLines = 0;    
    _msgLabel.text = @"当前网络不可用,请检查网络!";
    _msgLabel.textColor = [UIColor whiteColor];
    _msgLabel.textAlignment = NSTextAlignmentCenter;
    [self addSubview:_msgLabel];
}




@end


@implementation NetWorkTool

+ (instancetype)shareInstance{
    static NetWorkTool *network;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        network = [[NetWorkTool alloc] init];
    });
    return network;
}

+ (NSURLSessionDataTask *)request:(NSString *)url header:(NSDictionary *)header method:(NSString *)method params:(NSDictionary *)params success:(void (^)(id response))successBlock failure:(void(^)(NSError *error))failure{
    
    NSURL *Url = [NSURL URLWithString:url];
    NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:Url];
    [request setHTTPMethod:[method uppercaseString]];
    if(params){ //设置请求体
        [request setHTTPBody:[NSJSONSerialization dataWithJSONObject:params options:NSJSONWritingPrettyPrinted error:nil]];
    }//设置请求头
    if(header){
        [request setAllHTTPHeaderFields:header];
    }
    
    NSURLSessionDataTask *task = [[NSURLSession sessionWithConfiguration:[NSURLSessionConfiguration defaultSessionConfiguration]] dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if(!error){
            NSDictionary *result = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingAllowFragments error:nil];
            successBlock(result);
        }else{
            failure(error);
        }
    }];
    
    [task resume];
    return task;
}


@end
