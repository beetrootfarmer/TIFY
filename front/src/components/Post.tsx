import { useState } from 'react';
import DaumPostcodeEmbed from './DaumPostcodeEmbed';
import { useDaumPostcodePopup } from 'react-daum-postcode';
import search_h from '../assets/miri/miri-12.png';

const Postcode = (props: { setcompany: (arg0: any) => void; company: any }) => {
  const scriptUrl =
    'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
  const open = useDaumPostcodePopup(scriptUrl);

  const handleComplete = (data: any) => {
    let fullAddress = data.address;
    let zonecode = data.zonecode;
    console.log(fullAddress); // e.g. '서울 성동구 왕십리로2길 20 (성수동1가)'
    props.setcompany({
      ...props.company,
      address: fullAddress,
      zonecode: zonecode,
    });
  };

  const handleClick = () => {
    open({ onComplete: handleComplete });
  };

  return (
    <button type="button" onClick={handleClick} style={{ width: '40px' }}>
      {/* 주소찾기 */}
      <img src={search_h} />
    </button>
  );
};

export default Postcode;
