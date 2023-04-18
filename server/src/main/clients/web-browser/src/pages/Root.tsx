import './Root.scss';
import {Link} from 'react-router-dom';
import {Button, Dropdown, Form, Input, InputNumber, Modal, Space} from 'antd';
import {ChangeEvent, useState} from 'react';
import {DownOutlined} from '@ant-design/icons';
import * as React from 'react';
import {interest_service} from '../generated/protobuf-js';
import InterestService = interest_service.InterestService;
import {createService} from '../protos';
import RegisterInterestRequest = interest_service.RegisterInterestRequest;

export function Root() {
  const selectProfession = 'Select Profession';
  const otherProfession = 'Other (please describe below)';
  const defaultProfessions = [
    'K-12 District Leader/Administrator',
    'K-12 Educator',
    'Higher Education Leader/Administrator',
    'Higher Education Faculty Member',
    'Student',
    otherProfession,
  ];

  const [interestForm] = Form.useForm();
  const [interestFormOpen, setInterestFormOpen] = useState(false);
  const [professionSelection, setProfessionSelection] =
    useState(selectProfession);
  const [professionDescr, setProfessionDescr] = useState('');

  function submitInterestForm(values: {}) {
    setInterestFormOpen(false);
    const interestService = createService(InterestService, 'InterestService');
    const request = RegisterInterestRequest.fromObject(values);
    request.profession =
      professionSelection !== otherProfession
        ? professionSelection
        : professionDescr;
    interestService.registerInterest(request);
  }

  return (
    <>
      <header>
        <Link to="/" className="header-section header-section-left">
          <img src="/logo-white-on-orange.svg" />
        </Link>
        <div className="header-section header-section-center">
          <Link to="" className="nav-link">
            About
          </Link>
          <Link to="" className="nav-link">
            Our Mission
          </Link>
          <Link to="" className="nav-link">
            Projects
          </Link>
          <Link to="" className="nav-link">
            Blog
          </Link>
        </div>
        <div className="header-section header-section-right">
          <button className="light" onClick={() => setInterestFormOpen(true)}>
            I'm interested! Tell me more!
          </button>
        </div>
      </header>
      <main>
        <section
          className="primary-section"
          style={{display: 'flex', alignItems: 'flex-start', gap: '15px'}}
        >
          <div>
            <h1>Empowering&nbsp;Teachers, Engaging&nbsp;Students</h1>
            <div className="subheading">
              Unlock Your Classroom's Full Potential with Project Leo
            </div>
            <div className="summary">
              Designed with both teachers and students in mind, Project Leo
              provides an easy way to incorporate differentiation into your
              class, while giving students the freedom to pursue projects that
              truly inspire them. Say goodbye to the monotony of traditional
              schooling and hello to a purposeful and engaging learning
              experience.
            </div>
            <button className="light" onClick={() => setInterestFormOpen(true)}>
              I'm interested! Tell me more!
            </button>
          </div>
          <img src="/main_section_landing_page.png" />
        </section>
        <img src="/swoop.svg" style={{width: '100%'}} />
        <section className="details-with-image">
          <div>
            <h2>What is Project Leo?</h2>
            <p>
              Project Leo is an innovative educational tool designed to inspire
              both teachers and students. With Project Leo, teachers can easily
              incorporate differentiation into their classes, helping students
              find their own unique paths to success. By removing the rigidity
              of traditional classrooms, Project Leo gives students the freedom
              to pursue their passions and explore their own interests. By
              engaging students and empowering them to take control of their own
              learning, Project Leo helps to give school a purpose and prepares
              them for their future careers.
            </p>
          </div>
          <img src="/placeholder.jpg" />
        </section>
        <section className="details-with-image">
          <img src="/placeholder.jpg" />
          <div>
            <h2>Why Project Leo?</h2>
            <p>
              Project Leo not only provides project ideas that align with school
              learning objectives but also helps students develop career skills.
              By incorporating the students' interests, market demands, and
              skills, Project Leo offers a unique opportunity for students to
              explore their passions while preparing for their future careers.
              This approach empowers students to take ownership of their
              learning and enables them to develop a clear understanding of how
              their education can contribute to their professional goals.
            </p>
          </div>
        </section>
        <section className="details">
          <div>
            <h2>How does it work?</h2>
            <div className="timeline-with-image">
              <div>
                <h3>Students generate a unique project</h3>
                <p>
                  Project Leo allows students to take ownership of their
                  learning by generating personalized projects based on their
                  interests. Using AI technology, Project Leo aligns students'
                  interests with class objectives and possible career paths,
                  creating a truly customized and engaging learning experience.
                </p>
              </div>
              <img src="/placeholder.jpg" width="50%" />
            </div>
            <div className="timeline-with-image">
              <img src="/placeholder.jpg" width="50%" />
              <div>
                <h3>Students post their progress</h3>
                <p>
                  Project Leo allows students to take ownership of their
                  learning by generating personalized projects based on their
                  interests. Using AI technology, Project Leo aligns students'
                  interests with class objectives and possible career paths,
                  creating a truly customized and engaging learning experience.
                </p>
              </div>
            </div>
            <div className="timeline-with-image">
              <div>
                <h3>Teachers and professionals review and provide feedback</h3>
                <p>
                  Teachers and professionals in related fields can monitor
                  student progress and provide valuable feedback throughout the
                  project process. This collaborative approach promotes a
                  supportive learning environment where students can receive
                  guidance and support from multiple sources, including those
                  with specialized knowledge and expertise. With this approach,
                  students are able to work closely with both their teachers and
                  industry professionals, fostering both academic and personal
                  growth.
                </p>
              </div>
              <img src="/placeholder.jpg" width="50%" />
            </div>
            <div className="timeline-with-image">
              <img src="/placeholder.jpg" width="50%" />
              <div>
                <h3>Project Leo outputs final results</h3>
                <p>
                  Project Leo generates a summary of project results for both
                  students and teachers, providing a clear understanding of
                  project outcomes and serving as a tool for continuous
                  improvement. Students can use the information to reflect on
                  their learning and set future goals, while teachers can easily
                  grade projects and provide feedback to students, promoting a
                  collaborative and effective learning environment.
                </p>
              </div>
            </div>
            <div className="timeline-with-image">
              <div>
                <h3>Move on to the next project</h3>
                <p>
                  Project Leo generates an unlimited amount of engaging project
                  ideas, allowing students to explore new topics and areas of
                  interest, while building upon existing skills. This feature
                  promotes a love of lifelong learning, while keeping students
                  engaged and motivated to continue exploring and learning.
                </p>
              </div>
              <img src="/placeholder.jpg" width="50%" />
            </div>
          </div>
        </section>
        <section className="details">
          <div>
            <h2>Our Mission</h2>
            <p>
              At Project Leo, our mission is to empower teachers and students by
              providing a personalized and engaging learning experience. We aim
              to promote collaboration and accountability by enabling students
              to generate their own projects, showcase their progress, and
              receive feedback from teachers. Our goal is to foster a love of
              lifelong learning by providing a platform for students to explore
              their passions, build new skills, and prepare for future careers.
            </p>
          </div>
          <img src="/placeholder.jpg" />
        </section>
        <section className="details">
          <div>
            <h2>Contact Us</h2>
            <p>
              Interested in learning more about Project Leo? Are you
              experiencing any site issues? While you are exploring, please send
              all feedback and issues to Steven Eno -{' '}
              <a href="mailto:seno@davincischools.org">
                seno@davincischools.org
              </a>
            </p>
          </div>
        </section>
      </main>
      <footer>
        <Link
          to="https://www.freepik.com/free-vector/illustration-gallery-icon_2922280.htm#query=placeholder&position=0&from_view=search&track=sph"
          className="nav-link"
        >
          Placeholder image by rawpixel.com on Freepik
        </Link>
      </footer>
      <Modal
        title="I'm interested! Tell me more!"
        width="80%"
        open={interestFormOpen}
        closable={true}
        onCancel={() => setInterestFormOpen(false)}
        footer={null}
        forceRender
      >
        <Form
          form={interestForm}
          name="interested_form"
          labelWrap={false}
          labelCol={{span: 6}}
          wrapperCol={{span: 96}}
          onFinish={submitInterestForm}
        >
          <Form.Item
            label="First Name"
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your first name',
              },
            ]}
            name="firstName"
          >
            <Input name="firstName" maxLength={255} />
          </Form.Item>
          <Form.Item
            label="Last Name"
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your last name',
              },
            ]}
            name="lastName"
          >
            <Input name="lastName" maxLength={255} />
          </Form.Item>
          <Form.Item
            label="E-mail"
            rules={[
              {
                type: 'email',
                message: 'This e-mail address is not valid',
              },
              {
                required: true,
                message: 'Please enter your e-mail address',
              },
            ]}
            name="emailAddress"
          >
            <Input name="emailAddress" maxLength={254} />
          </Form.Item>
          <Form.Item
            label="Profession"
            required={true}
            rules={[
              {
                validator: () => {
                  if (professionSelection.trim() === selectProfession) {
                    return Promise.reject('Please select your profession');
                  }
                  return Promise.resolve();
                },
              },
            ]}
            name="professionSelection"
          >
            <Dropdown
              menu={{
                items: defaultProfessions.map(value => {
                  return {
                    key: value,
                    label: (
                      <div
                        onClick={() => {
                          setProfessionSelection(value);
                          interestForm.setFieldValue(
                            'professionSelection',
                            value
                          );
                        }}
                      >
                        {value}
                      </div>
                    ),
                  };
                }),
              }}
            >
              <Button>
                <Space>
                  {professionSelection}
                  <DownOutlined />
                </Space>
              </Button>
            </Dropdown>
          </Form.Item>
          <Form.Item
            label="Describe Profession"
            required={true}
            hidden={professionSelection !== otherProfession}
            rules={[
              {
                validator: () => {
                  if (professionSelection === otherProfession) {
                    if (professionDescr.trim() === '') {
                      return Promise.reject('Please describe your profession');
                    }
                  }
                  return Promise.resolve();
                },
              },
            ]}
            name="professionDescr"
          >
            <Input
              name="professionDescr"
              onChange={(e: ChangeEvent<HTMLInputElement>) =>
                setProfessionDescr(e.target.value)
              }
              value={professionDescr}
              maxLength={255}
            />
          </Form.Item>
          <Form.Item
            label="Reason for Interest"
            rules={[
              {
                required: true,
                whitespace: true,
                message: 'Please enter your reason for interest',
              },
            ]}
            name="reasonForInterest"
          >
            <Input.TextArea name="reasonForInterest" maxLength={8192} />
          </Form.Item>
          <Form.Item label="District Name" name="districtName">
            <Input name="districtName" maxLength={255} />
          </Form.Item>
          <Form.Item label="School Name" name="schoolName">
            <Input name="schoolName" maxLength={255} />
          </Form.Item>
          <Form.Item label="Anticipated Usage" style={{marginBottom: 0}}>
            <div style={{display: 'flex', gap: 10}}>
              <Form.Item label="Teachers" name="numTeachers">
                <InputNumber name="numTeachers" min={0} />
              </Form.Item>
              <Form.Item label="Students" name="numStudents">
                <InputNumber name="numTeachers" min={0} />
              </Form.Item>
            </div>
          </Form.Item>
          <Form.Item label="Address Line 1:" name="addressLine_1">
            <Input name="addressLine_1" maxLength={255} />
          </Form.Item>
          <Form.Item label="Address Line 2:" name="addressLine_2">
            <Input name="addressLine_2" maxLength={255} />
          </Form.Item>
          <Form.Item label="City" name="city">
            <Input name="city" maxLength={20} />
          </Form.Item>
          <Form.Item label="State" name="state">
            <Input name="state" maxLength={2} />
          </Form.Item>
          <Form.Item label="Zip Code" name="zipCode">
            <Input name="zipCode" maxLength={10} />
          </Form.Item>
          <Form.Item style={{textAlign: 'end'}}>
            <Button type="primary" htmlType="submit">
              Submit
            </Button>
            &nbsp;
            <Button type="default" onClick={() => setInterestFormOpen(false)}>
              Cancel
            </Button>
          </Form.Item>
        </Form>
      </Modal>
    </>
  );
}

//   const [messageOfTheDay, setMessageOfTheDay] = useState('');

//   const [ikigaiCenter, setIkigaiCenter] = useState<Coordinate | null>(null);
//   const [ikigaiDistance, setIkigaiDistance] = useState(0);
//   const [ikigaiSize, setIkigaiSize] = useState(0);

//   function updateIkigaiPosition() {
//     const main = document.getElementsByTagName('main').item(0) as HTMLElement;
//     if (main) {
//       setIkigaiCenter({
//         x: main.offsetLeft + main.offsetWidth / 2,
//         y:
//           main.offsetTop +
//           main.offsetHeight / 2 +
//           (Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.07,
//       });
//       setIkigaiSize((Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.8);
//       setIkigaiDistance(
//         (Math.min(main.offsetWidth, main.offsetHeight) / 2) * 0.8 * 0.4
//       );
//     }
//   }

//   // Updates the Ikigai position after layout and on window resize.
//   useEffect(() => {
//     updateIkigaiPosition();
//     window.addEventListener('resize', updateIkigaiPosition);
//   }, []);

//   // Loads and sets the message of the day.
//   useEffect(() => {
//     const motdService = protos.createService(
//       protos.message_of_the_day.MessageOfTheDayService,
//       'MessageOfTheDayService'
//     );

//     motdService
//       .getMessage(protos.message_of_the_day.MessageRequest.create({}))
//       .then((response: protos.message_of_the_day.MessageResponse) => {
//         setMessageOfTheDay(response.message!);
//       })
//       .catch((error: Error) => {
//         setMessageOfTheDay(`${error.name}: ${error.message}`);
//       });
//   }, []);

//   return (
//     <>
//       <IkigaiReel
//         id="IkigaiReel"
//         origin={ikigaiCenter}
//         size={ikigaiSize}
//         sizeDelta={ikigaiSize / 6}
//         distance={ikigaiDistance * 1.1}
//         distanceDelta={ikigaiDistance / 6}
//       />
//       {messageOfTheDay}
//       <div>
//         <button>Default</button>
//         <button className="light">Light</button>
//         <button className="primary">Primary</button>
//         <button className="info">Info</button>
//         <button className="delete">Delete</button>
//         <button className="prev">Prev</button>
//         <button className="next">Next</button>
//       </div>
//     </>
//   );
// }
