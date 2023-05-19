import './SignupForm.scss';

import {Button, Form, Input} from 'antd';
import {LockOutlined, MailOutlined, UserOutlined} from '@ant-design/icons';

export function SignupForm() {
  const [myProfileForm] = Form.useForm();

  function onFinish(values: {}) {
    myProfileForm.validateFields();
    console.log(values);
  }

  return (
    <>
      <Form form={myProfileForm} onFinish={onFinish}>
        <div>
          <Form.Item
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your first name.',
              },
            ]}
            name="firstName"
          >
            <Input
              placeholder="First Name"
              maxLength={255}
              autoComplete="given-name"
              prefix={<UserOutlined />}
            />
          </Form.Item>
          <Form.Item
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your last name.',
              },
            ]}
            name="lastName"
          >
            <Input
              placeholder="Last Name"
              maxLength={255}
              autoComplete="family-name"
              prefix={<UserOutlined />}
            />
          </Form.Item>
        </div>
        <Form.Item
          rules={[
            {
              required: true,
              whitespace: true,
              message: 'Please enter your email address..',
            },
            {
              type: 'email',
              message: 'This e-mail address is not valid',
            },
          ]}
          name="emailAddress"
        >
          <Input
            placeholder="Email Address"
            maxLength={255}
            autoComplete="email"
            prefix={<MailOutlined />}
          />
        </Form.Item>
        <Form.Item name="password">
          <Input
            placeholder="Password"
            maxLength={255}
            type="password"
            autoComplete="new-password"
            prefix={<LockOutlined />}
          />
        </Form.Item>
        <Form.Item name="verify_password">
          <Input
            placeholder="Re-enter Password"
            maxLength={255}
            type="password"
            autoComplete="new-password"
            prefix={<LockOutlined />}
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit">
            Submit
          </Button>
        </Form.Item>
      </Form>
    </>
  );
}
