//
//  CustomButton.m
//  IonicTest
//
//  Created by Mr.xiao on 2017/8/7.
//
//

#import "HLYCustomButton.h"
#define kImgWidth 60
@interface HLYCustomButton ()

@property (nonatomic, strong) UIImageView *imgView;
@property (nonatomic, strong) UILabel *btnNameLabel;

@end

@implementation HLYCustomButton

- (instancetype)initWithFrame:(CGRect)frame imgName:(NSString *)imgName titleString:(NSString *)title{
    if(self = [super initWithFrame:frame]){
        [self initSubViewsWithImgName:imgName title:title];
    }
    return self;
}

- (void)initSubViewsWithImgName:(NSString *)imgName title:(NSString *)title{
    CGSize size = self.frame.size;
    if(size.width == 0){
        return;
    }
    _imgView = [[UIImageView alloc] initWithFrame:CGRectMake((size.width-kImgWidth)/2, 0, kImgWidth, kImgWidth)];
    _imgView.image = [UIImage imageNamed:imgName];
    [self addSubview:_imgView];
    
    _btnNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, CGRectGetMaxY(_imgView.frame)+8, size.width, 25)];
    _btnNameLabel.font = [UIFont fontWithName:title size:14];
    _btnNameLabel.text = title;
    _btnNameLabel.textAlignment = NSTextAlignmentCenter;
    _btnNameLabel.textColor = [UIColor whiteColor];
    [self addSubview:_btnNameLabel];
}

- (void)setClickTitle:(NSString *)title isOpen:(BOOL)isOpen{
    dispatch_async(dispatch_get_main_queue(), ^{
        _btnNameLabel.text = title;
        if(isOpen){
            _imgView.image = [UIImage imageNamed:@"light_open"];
            _btnNameLabel.textColor = [UIColor colorWithRed:25/255.0f green:120/255.0f blue:209/255.0f alpha:1];
        }else{
            _btnNameLabel.textColor = [UIColor whiteColor];
            _imgView.image = [UIImage imageNamed:@"light_close"];
        }
    });
}

@end
