//
//  CustomButton.h
//  IonicTest
//
//  Created by Mr.xiao on 2017/8/7.
//
//

#import <UIKit/UIKit.h>

@interface HLYCustomButton : UIButton

- (instancetype)initWithFrame:(CGRect)frame imgName:(NSString *)imgName titleString:(NSString *)title;

- (void)setClickTitle:(NSString *)title isOpen:(BOOL)isOpen;

@end
