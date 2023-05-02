import './IkigaiBuilder.scss';
import {Button, Input, Layout, List, Modal} from 'antd';
import {Coordinate, Ikigai} from '../../../Ikigai/Ikigai';
import {useMeasure} from '@react-hookz/web';
import {ChangeEvent, useEffect, useState} from 'react';
import {
  class_management,
  createService,
  partial_text_openai_prompt,
  pl_types,
} from '../../../protos';
import {BarsOutlined, LoadingOutlined} from '@ant-design/icons';
import PartialTextOpenAiPromptService = partial_text_openai_prompt.PartialTextOpenAiPromptService;
import Prompt = partial_text_openai_prompt.GetSuggestionsRequest.Prompt;
import IAssignment = pl_types.IAssignment;
import ClassManagementService = class_management.ClassManagementService;
import {getCurrentUser} from '../../../utils/authentication';
import {useNavigate} from 'react-router';
import {DefaultPage} from '../../../libs/DefaultPage/DefaultPage';

const {Sider, Content} = Layout;

export function IkigaiBuilder() {
  const [user] = useState(
    getCurrentUser(() => {
      window.open('/login');
    })
  );
  const navigate = useNavigate();

  const [ikigaiCenterPosition, setIkigaiCenterPosition] =
    useState<Coordinate | null>(null);
  const [ikigaiDistanceToCategoryCenter, setIkigaiDistanceToCategoryCenter] =
    useState(0);
  const [ikigaiCategoryDiameter, setIkigaiCategoryDiameter] = useState(0);

  const [ikigaiContainerMeasure, ikigaiContainerMeasureRef] =
    useMeasure<HTMLDivElement>();
  const [useIkigaiResizeTimeout, setUseIkigaiResizeTimeout] = useState(true);
  const [ikigaiResizeTimeoutId, setIkigaiResizeTimeoutId] = useState<
    NodeJS.Timeout | undefined
  >(undefined);

  const [processing, setProcessing] = useState(false);

  const [lovesModalOpen, setLovesModalOpen] = useState(false);
  const [lovesValue, setLovesValue] = useState('');
  const [modalLovesValue, setModalLovesValue] = useState('');
  const [
    lovesGetRelatedSuggestionsEnabled,
    setLovesGetRelatedSuggestionsEnabled,
  ] = useState(true);
  const [lovesSuggestions, setLovesSuggestions] = useState<string[]>([]);

  const [worldNeedsModalOpen, setWorldNeedsModalOpen] = useState(false);
  const [assignments, setAssignments] = useState<IAssignment[]>([]);
  const [assignment, setAssignment] = useState<IAssignment | undefined>();
  const [modalAssignment, setModalAssignment] = useState<
    IAssignment | undefined
  >();

  const [paidForModalOpen, setPaidForModalOpen] = useState(false);

  const [goodAtModalOpen, setGoodAtModalOpen] = useState(false);
  const [goodAtValue, setGoodAtValue] = useState('');
  const [modalGoodAtValue, setModalGoodAtValue] = useState('');
  const [
    goodAtGetRelatedSuggestionsEnabled,
    setGoodAtGetRelatedSuggestionsEnabled,
  ] = useState(true);
  const [goodAtSuggestions, setGoodAtSuggestions] = useState<string[]>([]);

  // Resize and reposition the Ikigai diagram to be consistent with the window.
  function updateIkigaiPosition() {
    if (
      ikigaiContainerMeasureRef.current != null &&
      ikigaiContainerMeasure != null
    ) {
      setIkigaiCenterPosition({
        x:
          ikigaiContainerMeasureRef.current.offsetLeft +
          ikigaiContainerMeasure.width / 2,
        y:
          ikigaiContainerMeasureRef.current.offsetTop +
          ikigaiContainerMeasure.height / 2,
      });
      setIkigaiCategoryDiameter(
        Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2
      );
      setIkigaiDistanceToCategoryCenter(
        (Math.min(ikigaiContainerMeasure.width, ikigaiContainerMeasure.height) /
          2) *
          0.45
      );
    }
    setUseIkigaiResizeTimeout(false);
  }

  useEffect(() => {
    const classManagementService = createService(
      ClassManagementService,
      'ClassManagementService'
    );
    classManagementService
      .getStudentAssignments({userXId: user!.userXId!})
      .then(response => setAssignments(response.assignments));
  }, []);

  useEffect(() => {
    // TODO: This is a terrible solution. Revisit.
    if (useIkigaiResizeTimeout) {
      if (ikigaiResizeTimeoutId != null) {
        clearTimeout(ikigaiResizeTimeoutId!);
      }
      setIkigaiResizeTimeoutId(setTimeout(updateIkigaiPosition, 500));
    } else {
      updateIkigaiPosition();
    }
  }, [ikigaiContainerMeasureRef, ikigaiContainerMeasure]);

  function onLovesUpdate() {
    setLovesValue(modalLovesValue);
    setLovesModalOpen(false);
  }

  function getLovesRelatedSuggestions() {
    const partialTextOpenAiPromptService = createService(
      PartialTextOpenAiPromptService,
      'PartialTextOpenAiPromptService'
    );
    setLovesGetRelatedSuggestionsEnabled(false);
    partialTextOpenAiPromptService
      .getSuggestions({
        partialText: modalLovesValue,
        prompt: Prompt.SUGGEST_THINGS_YOU_LOVE,
        userXId: user!.userXId!,
      })
      .then(response => setLovesSuggestions(response.suggestions))
      .catch(() => setLovesSuggestions([]))
      .finally(() => setLovesGetRelatedSuggestionsEnabled(true));
  }

  function onWorldNeedsUpdate() {
    setAssignment(modalAssignment);
    setWorldNeedsModalOpen(false);
  }

  function onPaidForUpdate() {
    setPaidForModalOpen(false);
  }

  function onGoodAtUpdate() {
    setGoodAtValue(modalGoodAtValue);
    setGoodAtModalOpen(false);
  }

  function getGoodAtRelatedSuggestions() {
    const partialTextOpenAiPromptService = createService(
      PartialTextOpenAiPromptService,
      'PartialTextOpenAiPromptService'
    );
    setGoodAtGetRelatedSuggestionsEnabled(false);
    partialTextOpenAiPromptService
      .getSuggestions({
        partialText: modalGoodAtValue,
        prompt: Prompt.SUGGEST_THINGS_YOU_ARE_GOOD_AT,
        userXId: user!.userXId!,
      })
      .then(response => setGoodAtSuggestions(response.suggestions))
      .catch(() => setGoodAtSuggestions([]))
      .finally(() => setGoodAtGetRelatedSuggestionsEnabled(true));
  }

  function onSpinClick() {
    setProcessing(true);

    const classManagementService = createService(
      ClassManagementService,
      'ClassManagementService'
    );
    classManagementService
      .generateAssignmentProjects({
        userXId: user!.userXId,
        somethingYouLove: lovesValue,
        whatYouAreGoodAt: goodAtValue,
        assignmentId: assignment!.id!,
      })
      .finally(() => navigate('/projects/my-projects'));
  }

  return (
    <>
      <DefaultPage title="Ikigai Project Builder">
        <Layout style={{height: '100%'}}>
          <Content style={{borderRight: '#F0781F solid 1px', padding: '0.5em'}}>
            <div
              style={{width: '100%', height: '100%'}}
              ref={ikigaiContainerMeasureRef}
            >
              <div className="subtitle">Ikigai Builder</div>
              <div className="brief-instructions">
                Click each circle to edit.
              </div>
              <Ikigai
                id="ikigai-builder"
                centerPosition={ikigaiCenterPosition}
                categoryDiameter={ikigaiCategoryDiameter}
                distanceToCategoryCenter={ikigaiDistanceToCategoryCenter}
                radians={0}
                enabled={!processing}
                processing={processing}
                // LOVES Category.
                lovesResizeAndRotateElement={
                  <>
                    Something you <b>LOVE</b>
                    {lovesValue ? (
                      <>
                        <br />
                        <span
                          style={{fontSize: 'smaller', fontStyle: 'italic'}}
                        >
                          {lovesValue}
                        </span>
                      </>
                    ) : (
                      <></>
                    )}
                  </>
                }
                onLovesClick={() => {
                  setModalLovesValue(lovesValue);
                  setLovesModalOpen(true);
                }}
                lovesValueIsSet={lovesValue ? 0 : 1}
                // WORLD NEEDS category
                worldNeedsResizeAndRotateElement={
                  <>
                    What the world <b>NEEDS</b>
                    {assignment ? (
                      <>
                        <br />
                        <span
                          style={{fontSize: 'smaller', fontStyle: 'italic'}}
                        >
                          {assignment?.name}
                          <br />
                          <span style={{fontSize: 'smaller'}}>
                            (Eventually show EKS not assignment title)
                          </span>
                        </span>
                      </>
                    ) : (
                      <></>
                    )}
                  </>
                }
                onWorldNeedsClick={() => {
                  setModalAssignment(assignment);
                  setWorldNeedsModalOpen(true);
                }}
                worldNeedsValueIsSet={assignment ? 0 : 1}
                // PAID FOR Category.
                paidForResizeAndRotateElement={
                  <>
                    What you can be <b>PAID&nbsp;FOR</b>
                  </>
                }
                onPaidForClick={() => setPaidForModalOpen(true)}
                paidForValueIsSet={
                  !!lovesValue && !!assignment && !!goodAtValue ? 0 : 1
                }
                // GOOD AT Category.
                goodAtResizeAndRotateElement={
                  <>
                    What you are <b>GOOD&nbsp;AT</b>
                    {goodAtValue ? (
                      <>
                        <br />
                        <span
                          style={{fontSize: 'smaller', fontStyle: 'italic'}}
                        >
                          {goodAtValue}
                        </span>
                      </>
                    ) : (
                      <></>
                    )}
                  </>
                }
                onGoodAtClick={() => {
                  setModalGoodAtValue(goodAtValue);
                  setGoodAtModalOpen(true);
                }}
                goodAtValueIsSet={goodAtValue ? 0 : 1}
                showSpinButton={
                  !!lovesValue && !!assignment && !!goodAtValue && !processing
                }
                onSpinClick={onSpinClick}
              />
            </div>
          </Content>
          <Sider reverseArrow style={{padding: '0.5em'}}>
            <div>[TODO: Saved Projects]</div>
          </Sider>
        </Layout>

        <Modal
          title="Something you LOVE!"
          width="50%"
          open={lovesModalOpen}
          closable={true}
          onOk={onLovesUpdate}
          onCancel={() => {
            setLovesModalOpen(false);
          }}
        >
          <Input
            placeholder="What's something you LOVE?"
            maxLength={255}
            onChange={(e: ChangeEvent<HTMLInputElement>) =>
              setModalLovesValue(e.target.value)
            }
            value={modalLovesValue}
          />
          <Button
            style={{width: '100%'}}
            onClick={getLovesRelatedSuggestions}
            icon={
              lovesGetRelatedSuggestionsEnabled ? (
                <BarsOutlined />
              ) : (
                <LoadingOutlined />
              )
            }
            disabled={!lovesGetRelatedSuggestionsEnabled || !modalLovesValue}
          >
            Get Related Suggestions
          </Button>
          <List
            dataSource={lovesSuggestions}
            renderItem={suggestion => (
              <List.Item itemID={suggestion}>
                <div onClick={() => setModalLovesValue(suggestion)}>
                  {suggestion}
                </div>
              </List.Item>
            )}
          />
        </Modal>
        <Modal
          title="What the world NEEDS!"
          width="50%"
          open={worldNeedsModalOpen}
          closable={true}
          onCancel={() => setWorldNeedsModalOpen(false)}
          onOk={onWorldNeedsUpdate}
        >
          Select an assignment:
          <List
            dataSource={assignments}
            renderItem={assignment => (
              <List.Item itemID={assignment.id?.toString()}>
                <div
                  onClick={() => setModalAssignment(assignment)}
                  style={{
                    border:
                      modalAssignment?.id === assignment.id
                        ? '1px #f0781f solid'
                        : 'unset',
                    backgroundColor:
                      modalAssignment?.id === assignment.id
                        ? '#f4a062'
                        : 'unset',
                    color:
                      modalAssignment?.id === assignment.id ? 'white' : 'unset',
                    padding: '0.5em',
                    width: '100%',
                  }}
                >
                  <span style={{fontWeight: 'bold'}}>
                    Class: {assignment.classX?.name}
                  </span>
                  <br />
                  <span style={{fontStyle: 'italic'}}>
                    <span style={{fontWeight: 'bold'}}>Assignment:</span>{' '}
                    {assignment.name}
                  </span>
                </div>
              </List.Item>
            )}
          />
        </Modal>
        <Modal
          title="What you can be PAID FOR!"
          width="50%"
          open={paidForModalOpen}
          closable={true}
          onCancel={() => setPaidForModalOpen(false)}
          onOk={onPaidForUpdate}
        >
          TODO: Leave blank for now.
        </Modal>
        <Modal
          title="Something you are GOOD AT!"
          width="50%"
          open={goodAtModalOpen}
          closable={true}
          onOk={onGoodAtUpdate}
          onCancel={() => {
            setGoodAtModalOpen(false);
          }}
        >
          <Input
            placeholder="What's something you are GOOD AT?"
            maxLength={255}
            onChange={(e: ChangeEvent<HTMLInputElement>) =>
              setModalGoodAtValue(e.target.value)
            }
            value={modalGoodAtValue}
          />
          <Button
            style={{width: '100%'}}
            onClick={getGoodAtRelatedSuggestions}
            icon={
              goodAtGetRelatedSuggestionsEnabled ? (
                <BarsOutlined />
              ) : (
                <LoadingOutlined />
              )
            }
            disabled={!goodAtGetRelatedSuggestionsEnabled || !modalGoodAtValue}
          >
            Get Related Suggestions
          </Button>
          <List
            dataSource={goodAtSuggestions}
            renderItem={suggestion => (
              <List.Item itemID={suggestion}>
                <div onClick={() => setModalGoodAtValue(suggestion)}>
                  {suggestion}
                </div>
              </List.Item>
            )}
          />
        </Modal>
        <Modal
          centered
          width="50%"
          open={processing}
          closable={false}
          footer={null}
        >
          <div
            style={{
              textAlign: 'center',
              width: '100%',
            }}
          >
            Finding great projects! Please wait. This can take up to a minute.
          </div>
        </Modal>
      </DefaultPage>
    </>
  );
}
