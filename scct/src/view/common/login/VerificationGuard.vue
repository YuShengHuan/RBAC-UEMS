<template>
  <el-dialog
      v-model="userStore.humanVerificationStatus"
      title="人机验证"
      width="380px"
      align-center
      :before-close="handleBeforeClose"
      :show-close="false"
      class="verification-dialog"
  >
    <div style="margin: 10px;color: #2b91ea;">点击对应的文字/字母/数字，完成进度，即可进入</div>
    <div class="verification-content" :style="getRandomGradientBg(getRandomInRange(1,360))">
      <div class="puzzle-container">
        <template v-for="(char, index) in displayPuzzle">
          <el-popover
              placement="bottom"
              :show-arrow="false"
              trigger="hover"
              popper-style="min-width: 0px;padding: 0;width:50px;margin: 0;"
              :popper-options="{
                    modifiers: [
                      {
                        name: 'offset',
                        options: {
                          offset: [0, -50]  // [水平偏移, 垂直偏移]，均设为 0
                        }
                      }
                    ]
                  }"
          >
            <template #default>
              <div class="puzzle-char-show" @click="handleAddToInput(char)">{{ char }}</div>
            </template>
            <template #reference>
                       <span
                           :key="index"
                           class="puzzle-char"
                           :style="getUltimateRandom(getRandomInRange(1,360))"
                       >
                      {{ char }}
                    </span>
            </template>
          </el-popover>
        </template>
      </div>
    </div>
    <template #footer>
      <div class="el-button-footer">
        <div style="margin: 10px 0;display: flex;align-items: center;width: 100%;">
          <div class="paragraph-puzzle-contain">
            <div
                v-for="(puzzle,index) in paragraphPuzzle"
                :class="
                {'paragraph-puzzle':true,
                'paragraph-puzzle-success':puzzleClickArray.findIndex(item=>item===puzzle)===index,
                'paragraph-puzzle-error':puzzleClickArray.length>index&&puzzleClickArray.findIndex(item=>item===puzzle)!==index
                }"
                 >
              {{puzzle}}
            </div>
          </div>
          <el-button
              style="width: 70px;height: 35px;margin-left: 10px;"
              type="primary"
              @click="handleClearLastOne">
            清除
          </el-button>
        </div>
       <el-button style="width: 100%;" @click="generateNewPuzzle">
          换一个（太难了！）
        </el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import {useUserStore} from "../../../stores/user";
const userStore=useUserStore()
const puzzleClickArray = ref([]);
const displayPuzzle = ref<string[]>([]);
const targetPuzzle=ref('');
const paragraphPuzzle=ref([])
// 生成随机字符串（增加部分易混淆字符，提升干扰性）
/**
 * 随机生成汉字函数
 * @param count 生成汉字的数量（默认1个）
 * @returns 随机汉字字符串（如 count=3 时返回 "张三李"）
 */
const generateRandomChinese = (count: number = 1): string => {
  // 汉字 Unicode 核心范围：0x4E00（一）至 0x9FA5（龥），共20902个常用汉字
  const start = 0x4E00;
  const end = 0x9FA5;
  let result = '';

  for (let i = 0; i < count; i++) {
    // 生成范围内的随机 Unicode 编码
    const randomCode = Math.floor(Math.random() * (end - start + 1)) + start;
    // 转为汉字
    result += String.fromCodePoint(randomCode);
  }

  return result;
};

/**
 * 增强版：混合生成（汉字+字母+数字），适合验证码等场景
 * @param count 总字符数
 * @param chineseRatio 汉字占比（0-1，如0.5表示一半汉字）
 * @returns 混合字符字符串
 */
const generateMixedChars = (count: number = 4, chineseRatio: number = 0.5): string => {
  const chineseCount = Math.floor(count * chineseRatio);
  const otherCount = count - chineseCount;
  const lettersNumbers = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

  // 生成汉字
  let result = generateRandomChinese(chineseCount);

  // 生成字母/数字
  for (let i = 0; i < otherCount; i++) {
    const randomIdx = Math.floor(Math.random() * lettersNumbers.length);
    result += lettersNumbers[randomIdx];
  }

  // 打乱顺序（可选，增强随机性）
  return result.split('').sort(() => Math.random() - 0.5).join('');
};
function getRandomInRange(min, max) {
  // 确保 min <= max，避免参数传反
  [min, max] = [Math.floor(min), Math.floor(max)];
  return Math.floor(Math.random() * (max - min + 1)) + min;
}
function getRandomBackgroundColor() {
  // 辅助函数：生成 0-255 之间的随机整数
  const randomByte = () => Math.floor(Math.random() * 256);

  // 辅助函数：生成随机的 RGB 颜色字符串
  const getRandomRgb = () => `rgb(${randomByte()}, ${randomByte()}, ${randomByte()})`;

    // 生成渐变色
    const color1 = getRandomRgb();
    const color2 = getRandomRgb();
  const color3 = getRandomRgb();
    // 随机决定是线性渐变还是径向渐变
    const isLinear = Math.random() > 0.5;

    if (isLinear) {
      // 线性渐变：随机角度（0deg 到 360deg）
      const angle = Math.floor(Math.random() * 361);
      return `linear-gradient(${angle}deg, ${color1}, ${color2},${color3})`;
    } else {
      // 径向渐变：随机起始点
      const position = `${Math.floor(Math.random() * 101)}% ${Math.floor(Math.random() * 101)}%`;
      return `radial-gradient(circle at ${position}, ${color1}, ${color2})`;
    }
}

const getUltimateRandom = (index: number): object => {
  // 基础随机种子（基于索引，确保同一次生成的样式一致）
  const baseSeed = index * 1000;
  const random = (seed: number) => (Math.sin(seed) * 10000) % 1;

  // 1. 随机旋转（-15° 到 15°，倾斜干扰）
  const rotateAngle = (random(baseSeed + 1) - 0.5) * 30;

  // 2. 随机缩放（0.9 到 1.1 倍，大小干扰）
  const scale = 0.9 + random(baseSeed + 2) * 0.2;

  // 3. 随机倾斜（-5° 到 5°，剪切干扰）
  const skewX = (random(baseSeed + 3) - 0.5) * 10;
  const skewY = (random(baseSeed + 4) - 0.5) * 10;

  // 4. 字体颜色渐变（多色渐变，颜色干扰）
  const hue1 = Math.floor(random(baseSeed + 5) * 360);
  const hue2 = Math.floor(random(baseSeed + 6) * 360);
  const hue3 = Math.floor(random(baseSeed + 7) * 360); // 新增第三色
  const gradientDirection = Math.floor(random(baseSeed + 8) * 360);
  const textGradient = `linear-gradient(${gradientDirection}deg, hsl(${hue1}, 80%, 50%), hsl(${hue2}, 80%, 50%), hsl(${hue3}, 80%, 50%))`;

  // 5. 背景颜色渐变（复杂渐变，背景干扰）
  const bgHue1 = Math.floor(random(baseSeed + 9) * 360);
  const bgHue2 = Math.floor(random(baseSeed + 10) * 360);
  const bgHue3 = Math.floor(random(baseSeed + 11) * 360);
  const bgHue4 = Math.floor(random(baseSeed + 12) * 360); // 新增第四色
  const bgGradient = `linear-gradient(${Math.floor(random(baseSeed + 13) * 360)}deg,
    hsl(${bgHue1}, 30%, 85%),
    hsl(${bgHue2}, 30%, 90%),
    hsl(${bgHue3}, 30%, 85%),
    hsl(${bgHue4}, 30%, 90%)
  )`;


  // 7. 随机阴影（多阴影叠加+新增内层阴影，立体干扰）
  const shadowColor1 = `hsla(${Math.floor(random(baseSeed + 17) * 360)}, 60%, 50%, 0.3)`;
  const shadowColor2 = `hsla(${Math.floor(random(baseSeed + 18) * 360)}, 60%, 50%, 0.2)`;
  const shadowColor3 = `hsla(${Math.floor(random(baseSeed + 19) * 360)}, 60%, 50%, 0.1)`; // 新增第三层阴影
  const innerShadow = `inset ${Math.floor(random(baseSeed + 20) * 3)}px ${Math.floor(random(baseSeed + 21) * 3)}px ${Math.floor(random(baseSeed + 22) * 6)}px ${shadowColor3}`; // 新增内层阴影
  const boxShadow = `${Math.floor(random(baseSeed + 23) * 4)}px ${Math.floor(random(baseSeed + 24) * 4)}px ${Math.floor(random(baseSeed + 25) * 6)}px ${shadowColor1},
                     ${Math.floor(random(baseSeed + 26) * -4)}px ${Math.floor(random(baseSeed + 27) * -4)}px ${Math.floor(random(baseSeed + 28) * 6)}px ${shadowColor2},
                     ${innerShadow}`;

  // 8. 文字模糊（增强模糊范围+随机方向模糊，轻微模糊干扰）
  const textBlur = random(baseSeed + 29) * 0.8; // 扩大到 0-0.8px
  const textBlurDirection = Math.random() > 0.5 ? 'horizontal' : 'vertical'; // 新增模糊方向
  const textShadow = `0 0 ${textBlur}px rgba(0,0,0,0.3), ${textBlurDirection === 'horizontal' ? `${textBlur}px 0 0 rgba(0,0,0,0.2)` : `0 ${textBlur}px 0 rgba(0,0,0,0.2)`}`;

  // 9. 背景噪点（增强密度+新增形状噪点，颗粒干扰）
  const noiseDensity = Math.floor(random(baseSeed + 30) * 10) + 8; // 8-18 个噪点
  let noiseBackground = '';
  for (let i = 0; i < noiseDensity; i++) {
    const noiseX = Math.floor(random(baseSeed + 31 + i) * 45) + 2;
    const noiseY = Math.floor(random(baseSeed + 32 + i) * 45) + 2;
    const noiseSize = Math.floor(random(baseSeed + 33 + i) * 4) + 1; // 1-4px
    const noiseShape = Math.random() > 0.5 ? 'circle' : 'ellipse'; // 新增椭圆/圆形噪点
    const noiseColor = `hsla(${Math.floor(random(baseSeed + 34 + i) * 360)}, 40%, 50%, ${0.2 + random(baseSeed + 35 + i) * 0.3})`;
    noiseBackground += `${noiseShape === 'circle' ? 'radial-gradient(circle' : `radial-gradient(ellipse ${Math.floor(random(baseSeed + 36 + i) * 3 + 1)}px ${Math.floor(random(baseSeed + 37 + i) * 2 + 1)}px`}, ${noiseColor} ${noiseSize}px, transparent ${noiseSize}px) ${noiseX}px ${noiseY}px,`;
  }
  noiseBackground = noiseBackground.slice(0, -1);

  // 10. 文字描边（双层描边+随机透明度，轮廓干扰）
  const textStrokeColor = `hsl(${Math.floor(random(baseSeed + 38) * 360)}, 70%, 40%)`;
  const textStrokeColor2 = `hsla(${Math.floor(random(baseSeed + 39) * 360)}, 70%, 60%, 0.5)`; // 新增第二层描边色
  const textStrokeWidth = random(baseSeed + 40) * 0.5 + 0.2;
  const textStrokeWidth2 = textStrokeWidth + 0.1; // 外层描边略宽

  // 11. 随机内边距（扩大范围+不对称内边距，位置微调干扰）
  const paddingTop = Math.floor(random(baseSeed + 41) * 4); // 0-3px
  const paddingRight = Math.floor(random(baseSeed + 42) * 4);
  const paddingBottom = Math.floor(random(baseSeed + 43) * 4);
  const paddingLeft = Math.floor(random(baseSeed + 44) * 4);
  const padding = `${paddingTop}px ${paddingRight}px ${paddingBottom}px ${paddingLeft}px`;

  // 12. 新增：文字随机变形（水平/垂直拉伸）
  const textScaleX = 0.85 + random(baseSeed + 45) * 0.3; // 0.85-1.15 水平拉伸
  const textScaleY = 0.85 + random(baseSeed + 46) * 0.3; // 0.85-1.15 垂直拉伸

  // 13. 新增：背景网格纹理（半透明交叉线）
  const gridSize = 6 + Math.floor(random(baseSeed + 47) * 6); // 6-11px
  const gridColor = `hsla(${Math.floor(random(baseSeed + 48) * 360)}, 50%, 50%, ${0.05 + random(baseSeed + 49) * 0.1})`;
  const gridGradientX = `linear-gradient(to right, ${gridColor} 1px, transparent 1px)`;
  const gridGradientY = `linear-gradient(to bottom, ${gridColor} 1px, transparent 1px)`;
  const gridTexture = `${gridGradientX}, ${gridGradientY}`;

  // 14. 新增：文字阴影叠加（多色阴影）
  const textShadowColor2 = `hsla(${Math.floor(random(baseSeed + 50) * 360)}, 80%, 60%, 0.2)`;
  const extraTextShadow = `${Math.floor(random(baseSeed + 51) * 2 - 1)}px ${Math.floor(random(baseSeed + 52) * 2 - 1)}px ${Math.floor(random(baseSeed + 53) * 2)}px ${textShadowColor2}`;

  // 15. 新增：边框圆角随机（单独圆角控制）
  const borderRadius = `${Math.floor(random(baseSeed + 54) * 8)}px ${Math.floor(random(baseSeed + 55) * 8)}px ${Math.floor(random(baseSeed + 56) * 8)}px ${Math.floor(random(baseSeed + 57) * 8)}px`; // 0-7px 四角不同圆角

  // 16. 新增：背景条纹干扰（斜向半透明条纹）
  const stripeAngle = Math.floor(random(baseSeed + 58) * 180); // 0-179°
  const stripeColor1 = `hsla(${Math.floor(random(baseSeed + 59) * 360)}, 40%, 70%, ${0.1 + random(baseSeed + 60) * 0.15})`;
  const stripeColor2 = `hsla(${Math.floor(random(baseSeed + 61) * 360)}, 40%, 80%, ${0.1 + random(baseSeed + 62) * 0.15})`;
  const stripeSize = 4 + Math.floor(random(baseSeed + 63) * 6); // 4-9px
  const stripeTexture = `linear-gradient(${stripeAngle}deg, ${stripeColor1} ${stripeSize}px, ${stripeColor2} ${stripeSize}px, ${stripeColor2} ${stripeSize * 2}px)`;

  // 17. 新增：文字大小写随机（部分字符大小写转换）
  const textTransform = Math.random() > 0.7 ? (Math.random() > 0.5 ? 'uppercase' : 'lowercase') : 'none';

  // 18. 新增：背景叠加半透明色块（随机位置）
  const overlayX = Math.floor(random(baseSeed + 64) * 30) + 10; // 10-39px
  const overlayY = Math.floor(random(baseSeed + 65) * 30) + 10; // 10-39px
  const overlaySize = 15 + Math.floor(random(baseSeed + 66) * 20); // 15-34px
  const overlayColor = `hsla(${Math.floor(random(baseSeed + 67) * 360)}, 30%, 50%, ${0.05 + random(baseSeed + 68) * 0.1})`;
  const overlayTexture = `radial-gradient(circle at ${overlayX}px ${overlayY}px, ${overlayColor}, transparent ${overlaySize}px)`;

  // 19. 新增：字体变体干扰（斜体/正常随机）
  const fontStyle = Math.random() > 0.6 ? 'italic' : 'normal';

  // 20. 新增：背景混合模式（随机混合效果）
  const blendModes = ['normal', 'multiply', 'overlay', 'soft-light'];
  const backgroundBlendMode = blendModes[Math.floor(random(baseSeed + 69) * blendModes.length)];

  // 组合所有样式（合并新增效果）
  return {
    // 变形相关（新增文字拉伸）
    transform: `rotate(${rotateAngle}deg) scale(${scale}) skew(${skewX}deg, ${skewY}deg) scaleX(${textScaleX}) scaleY(${textScaleY})`,
    transformOrigin: 'center center',

    // 文字相关（新增双层描边、多色阴影、大小写转换、斜体）
    backgroundImage: `${textGradient}, ${noiseBackground}`,
    WebkitBackgroundClip: 'text',
    backgroundClip: 'text',
    color: 'transparent',
    textShadow: `${textShadow}, ${extraTextShadow}`,
    WebkitTextStroke: `${textStrokeWidth}px ${textStrokeColor}`,
    textStroke: `${textStrokeWidth}px ${textStrokeColor}`,
    WebkitTextStrokeWidth: `${textStrokeWidth2}px, ${textStrokeWidth}px`,
    WebkitTextStrokeColor: `${textStrokeColor2}, ${textStrokeColor}`,
    textTransform: textTransform,
    fontStyle: fontStyle,

    // 背景和边框（新增网格、条纹、叠加层、混合模式、自定义圆角）
    backgroundColor: getRandomBackgroundColor(),
    backgroundImage: `${bgGradient}, ${noiseBackground}, ${gridTexture}, ${stripeTexture}, ${overlayTexture}`,
    backgroundSize: `${gridSize}px ${gridSize}, auto, auto, auto`,
    backgroundBlendMode: backgroundBlendMode,
    borderRadius: borderRadius,

    // 布局相关（更新内边距为不对称模式）
    boxShadow: boxShadow,
    padding: padding,
    overflow: 'hidden',

    // 字体相关（新增更多字体选项）
    fontFamily: ['"Courier New"', '"Georgia"', '"Times New Roman"', '"Arial"', '"Impact"', '"Comic Sans MS"'][Math.floor(random(baseSeed + 70) * 6)],
    fontSize: `${22 + Math.floor(random(baseSeed + 71) * 4)}px`, // 22-25px
    fontWeight: Math.floor(random(baseSeed + 72) * 4) + 500, // 500-800 加粗
    letterSpacing: `${(random(baseSeed + 73) - 0.5) * 2.5}px`, // 扩大到 -1.25 到 1.25px 字间距

    // 新增：抗锯齿优化（避免模糊边缘）
    WebkitFontSmoothing: 'antialiased',
    MozOsxFontSmoothing: 'grayscale',
  };
};
// 辅助函数：生成 0-255 随机整数
const randomByte = () => Math.floor(Math.random() * 256);
// 辅助函数：生成随机 RGB 颜色（兼容 0-255 范围）
const getRandomRgb = () => `rgb(${randomByte()}, ${randomByte()}, ${randomByte()})`;

/**
 * 背景渐变干扰生成函数
 * @param index 索引（用于种子，确保同次生成样式一致）
 * @returns 背景渐变样式对象（含随机角度、随机颜色数量）
 */
const getRandomGradientBg = (index: number): object => {
  return {
    background: `linear-gradient(${getRandomInRange(0,255)}deg,
    ${getRandomRgb()},
    ${getRandomRgb()},
    ${getRandomRgb()})`
  };
};

const randomPickChars = (ar, count)=>{
  // 将字符串转为字符数组，便于打乱顺序
  const charArr = [...ar]
  // 洗牌算法：随机打乱字符数组顺序（确保每个字符被选中概率均等）
  for (let i = charArr.length - 1; i > 0; i--) {
    const randomIdx = Math.floor(Math.random() * (i + 1));
    // 交换当前位置与随机位置的字符
    [charArr[i], charArr[randomIdx]] = [charArr[randomIdx], charArr[i]];
  }
  return charArr.slice(0, count).join('');
};

// 生成新的验证 puzzle
const generateNewPuzzle = () => {
  // 生成一个 12 位的随机字符串
  // 构建用于显示的数组
  displayPuzzle.value = generateMixedChars(12).split('');
  targetPuzzle.value=randomPickChars(displayPuzzle.value,4)
  paragraphPuzzle.value=targetPuzzle.value.split('')
};
const handleAddToInput=(char)=>{
   if(puzzleClickArray.value.length<4) {
        puzzleClickArray.value.push(char)
   }
   if(puzzleClickArray.value.join("")===targetPuzzle.value){
      userStore.setHumanVerificationStatus(false)
      ElMessage.success("你已经通过人机验证")
   }
}
const handleClearLastOne=()=>{
    puzzleClickArray.value.pop()
}
// 弹框关闭前的钩子，防止用户通过其他方式关闭
const handleBeforeClose = () => {
  ElMessage.info('请先完成人机验证，证明你不是机器～');
};

// 组件挂载时生成第一个 puzzle 并聚焦输入框
onMounted(() => {
  generateNewPuzzle();
});

</script>

<style scoped>
.verification-dialog {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
}

.verification-content {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  user-select: none;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  -webkit-user-drag: none;
  -webkit-touch-callout: none;
}

.puzzle-container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  width: 100%;
  height: 100%;
}

.puzzle-char {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 50px;
  height: 50px;
  transition: all 0.3s ease;
  cursor: pointer;
  margin: 10px;
  z-index: 10;
}
.puzzle-char-show{
  cursor: pointer;
  user-select: none;
  font-size: 30px;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.el-button-footer {
  display: flex;
  align-items: center;
  flex-direction: column;
}
.paragraph-puzzle-contain{
  width: calc(100% - 80px);
  background-color: #c5d4d5;
  height: 35px;display: flex;align-items: center;
}
.paragraph-puzzle{
  height: 100%;
  width:25%;
  display: flex;
  align-items: center;
  justify-content: center;
}
.paragraph-puzzle-success{
   background-color: green;
  color: #FFFFFF;
}
.paragraph-puzzle-error{
   background-color: #d31313;
  color: #FFFFFF;
}
</style>