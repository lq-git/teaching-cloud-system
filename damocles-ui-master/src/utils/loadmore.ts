// 设置默认溢出显示数量
const spillDataNum = 20;

// 设置隐藏函数
let timeout: any = false;
const setRowDisableNone = function (topNum: number, showRowNum: number, binding: any) {
    if (timeout) {
        clearTimeout(timeout);
    }
    timeout = setTimeout(() => {
        binding.value.call(null, topNum, topNum + showRowNum + spillDataNum);
    });
};

export default {
    name: 'loadmore',
    componentUpdated: function (el: any, binding: any, vnode: any, oldVnode: any) {
        setTimeout(() => {
            // const dataSize = vnode.data.attrs['data-size'];
            // const oldDataSize = oldVnode.data.attrs['data-size'];
            const dataSize = vnode.props.datasize;
            const oldDataSize = oldVnode.props.datasize;
            if (dataSize === oldDataSize) {
                return;
            }
            const selectWrap = el.querySelector('.el-table__body-wrapper');
            const selectTbody = selectWrap.querySelector('table tbody');
            const selectRow = selectWrap.querySelector('table tr');
            if (!selectRow) {
                return;
            }
            const rowHeight = selectRow.clientHeight;
            const showRowNum = Math.round(selectWrap.clientHeight / rowHeight);

            const createElementTR = document.createElement('tr');
            const createElementTRHeight = (dataSize - showRowNum - spillDataNum) * rowHeight;
            createElementTR.setAttribute('style', `height: ${createElementTRHeight}px;`);
            selectTbody.append(createElementTR);

            // 监听滚动后事件
            selectWrap.addEventListener('scroll', function () {
                let topPx = selectWrap.scrollTop - spillDataNum * rowHeight;
                let topNum = Math.round(topPx / rowHeight);
                const minTopNum = dataSize - spillDataNum - showRowNum;
                if (topNum > minTopNum) {
                    topNum = minTopNum;
                }
                if (topNum < 0) {
                    topNum = 0;
                    topPx = 0;
                }
                selectTbody.setAttribute('style', `transform: translateY(${topPx}px)`);
                createElementTR.setAttribute('style', `height: ${createElementTRHeight - topPx > 0 ? createElementTRHeight - topPx : 0}px;`);
                setRowDisableNone(topNum, showRowNum, binding);
            })
        });
    }
};