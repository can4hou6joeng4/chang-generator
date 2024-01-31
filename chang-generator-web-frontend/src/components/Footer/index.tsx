import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import '@umijs/max';
import React from 'react';

const Footer: React.FC = () => {
  const defaultMessage = '啵啵肠';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      style={{
        background: 'none',
      }}
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'codeNav',
          title: '我的掘金',
          href: 'https://juejin.cn/user/1187904004821262',
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: '我的博客',
          href: 'https://blog.bobochang.work',
          blankTarget: true,
        },
        {
          key: 'github',
          title: (
            <>
              <GithubOutlined /> 啵啵肠
            </>
          ),
          href: 'https://github.com/bobochangzzz',
          blankTarget: true,
        },
      ]}
    />
  );
};
export default Footer;
