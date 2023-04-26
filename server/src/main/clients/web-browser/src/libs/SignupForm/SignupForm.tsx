import './SignupForm.scss';

import {Form, Input} from 'antd';
import {LockOutlined, MailOutlined, UserOutlined} from '@ant-design/icons';

export function SignupForm() {
  const [myProfileForm] = Form.useForm();

  return (
    <>
      <Form form={myProfileForm}>
        <div>
          <Form.Item
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your first name.',
              },
            ]}
            name="first_name"
          >
            <div>
              <UserOutlined />
              <Input
                name="first_name"
                placeholder="First Name"
                maxLength={255}
                autoComplete="given-name"
              />
            </div>
          </Form.Item>
          <Form.Item
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your last name.',
              },
            ]}
            name="last_name"
          >
            <div>
              <UserOutlined />
              <Input
                name="last_name"
                placeholder="Last Name"
                maxLength={255}
                autoComplete="family-name"
              />
            </div>
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
          name="email_address"
        >
          <div>
            <MailOutlined />
            <Input
              name="email_address"
              placeholder="Email Address"
              maxLength={255}
              autoComplete="email"
            />
          </div>
        </Form.Item>
        <Form.Item name="password">
          <div>
            <LockOutlined />
            <Input
              name="password"
              placeholder="Password"
              maxLength={255}
              type="password"
              autoComplete="new-password"
            />
          </div>
        </Form.Item>
        <Form.Item name="verifyPassword">
          <div>
            <LockOutlined />
            <Input
              name="verifyPassword"
              placeholder="Re-enter Password"
              maxLength={255}
              type="password"
              autoComplete="new-password"
            />
          </div>
        </Form.Item>
      </Form>
    </>
  );
}
