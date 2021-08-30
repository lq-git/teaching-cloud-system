import { request, responseEntity } from '../utils/request'
import Dict from '../types/dict'
const loadDictData = async (callback: Function) => {
  await request("/dict/list").then((response) => {
    const res = response as responseEntity;
    const dict = res.data as Dict[];
    callback(dict);
  });
};

const getDictByType = (dict: Array<any>, type: string) => {
  return dict.filter((item) => item.type === type);
}

const convertDictList = (data: Array<any>, type?: string) => {
  if (type) data = getDictByType(data, type);
  const container = [] as any;
  for (let i = 0; i < data.length; i++) {
    const type = data[i].type;
    if (!container[type]) {
      container[type] = [];
    }
    container[type].push(data[i]);
  }
  return container;
}

const convertDictMap = (data: Array<any>, type?: string) => {
  const dict = {} as any;
  const container = convertDictList(data, type);
  let key = "" as any;
  for (key in container) {
    const attr = {} as any;
    for (const obj of container[key]) {
      attr[obj.code] = obj.text;
    }
    dict[key] = attr;
  }
  return dict;
}

export {
  loadDictData,
  convertDictList,
  convertDictMap
}